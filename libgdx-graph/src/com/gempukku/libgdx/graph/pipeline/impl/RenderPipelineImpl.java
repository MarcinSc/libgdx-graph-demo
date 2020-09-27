package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;

import java.util.Iterator;

public class RenderPipelineImpl implements RenderPipeline {
    private FrameBuffer depthBuffer;
    private BufferCopyHelper bufferCopyHelper = new BufferCopyHelper();
    private FrameBuffer mainBuffer;

    private Array<FrameBuffer> oldFrameBuffers = new Array<FrameBuffer>();
    private Array<FrameBuffer> newFrameBuffers = new Array<FrameBuffer>();
    private boolean needsDepthBuffer;

    public RenderPipelineImpl(boolean needsDepthBuffer) {
        this.needsDepthBuffer = needsDepthBuffer;
    }

    @Override
    public FrameBuffer getDepthFrameBuffer() {
        return depthBuffer;
    }

    public void startFrame(int width, int height) {
        if (needsDepthBuffer) {
            depthBuffer = getNewFrameBuffer(width, height, Pixmap.Format.RGBA8888);
            depthBuffer.begin();
            Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            depthBuffer.end();
        }
    }

    public void endFrame() {
        if (depthBuffer != null) {
            returnFrameBuffer(depthBuffer);
            depthBuffer = null;
        }
        for (FrameBuffer freeFrameBuffer : oldFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        oldFrameBuffers.clear();
        oldFrameBuffers.addAll(newFrameBuffers);
        newFrameBuffers.clear();
    }

    public void cleanup() {
        for (FrameBuffer freeFrameBuffer : oldFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        for (FrameBuffer freeFrameBuffer : newFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        oldFrameBuffers.clear();
        newFrameBuffers.clear();
        bufferCopyHelper.dispose();
    }

    @Override
    public void setCurrentBuffer(FrameBuffer frameBuffer) {
        mainBuffer = frameBuffer;
    }

    @Override
    public FrameBuffer getNewFrameBuffer(FrameBuffer takeSettingsFrom) {
        return getNewFrameBuffer(takeSettingsFrom.getWidth(), takeSettingsFrom.getHeight(),
                takeSettingsFrom.getColorBufferTexture().getTextureData().getFormat());
    }

    @Override
    public FrameBuffer getNewFrameBuffer(int width, int height, Pixmap.Format format) {
        FrameBuffer buffer = extractFrameBuffer(width, height, this.newFrameBuffers);
        if (buffer != null) return buffer;
        buffer = extractFrameBuffer(width, height, this.oldFrameBuffers);
        if (buffer != null) return buffer;

        GLFrameBuffer.FrameBufferBuilder frameBufferBuilder = new GLFrameBuffer.FrameBufferBuilder(width, height);
        frameBufferBuilder.addBasicColorTextureAttachment(format);
        frameBufferBuilder.addBasicDepthRenderBuffer();
        return frameBufferBuilder.build();
    }

    @Override
    public BufferCopyHelper getBufferCopyHelper() {
        return bufferCopyHelper;
    }

    private FrameBuffer extractFrameBuffer(int width, int height, Array<FrameBuffer> frameBuffers) {
        Iterator<FrameBuffer> iterator = frameBuffers.iterator();
        while (iterator.hasNext()) {
            FrameBuffer buffer = iterator.next();
            if (buffer.getWidth() == width && buffer.getHeight() == height) {
                iterator.remove();
                return buffer;
            }
        }
        return null;
    }

    @Override
    public void returnFrameBuffer(FrameBuffer frameBuffer) {
        newFrameBuffers.add(frameBuffer);
    }

    public FrameBuffer getCurrentBuffer() {
        return mainBuffer;
    }

    public void dispose() {
        cleanup();
    }
}
