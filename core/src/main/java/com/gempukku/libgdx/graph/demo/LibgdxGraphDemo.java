package com.gempukku.libgdx.graph.demo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.demo.script.ActorScript;
import com.gempukku.libgdx.graph.demo.script.MovieScript;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class LibgdxGraphDemo extends ApplicationAdapter {
    private Array<Disposable> disposables = new Array<>();

    private PipelineRenderer pipelineRenderer;
    private Stage stage;
    private Script script;
    private String hangarFloorId;
    private String hangarWallId;
    private String luminarisId;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Label subtitleLabel = createStageSubtitleLabel();

        Camera camera = createCamera();
        GraphShaderModels models = createModels();
        pipelineRenderer = loadPipelineRenderer(models, camera, stage);

        script = createScript(subtitleLabel, camera, models, pipelineRenderer);
        // TODO: Temp - move to hangar scene
        script.update(6f);

        Gdx.input.setInputProcessor(stage);
    }

    private MovieScript createScript(Label subtitleLabel, Camera camera, GraphShaderModels models, PipelineRenderer pipelineRenderer) {
        MovieScript movieScript = new MovieScript(subtitleLabel, camera, models, pipelineRenderer);
        // Introduction
        movieScript.setSubtitleText(0f, Color.WHITE, "This is not a game.\nThis is a libGDX Graph Demo.\nAll graphical assets were free and not created by me.");
        movieScript.setSubtitleText(5f, Color.WHITE, "");
        movieScript.setSubtitleText(5.5f, new Color(0.8f, 0.8f, 1f, 1f), "A few centuries ago, in a far away galaxy...");

        // Hangar scene
        float hangarSceneStart = 6f;
        float hangarSceneLength = 10f;
        movieScript.setPipelineFloatProperty("Blackout", hangarSceneStart, 3f, 1, 0);
        movieScript.setPipelineFloatProperty("Blur", hangarSceneStart, 3f, 10, 0);
        movieScript.setSubtitleText(9f, Color.WHITE, "");
        movieScript.setSubtitleText(9.5f, Color.WHITE, "- This is GDX-255 requesting permission to enter the hangar.");
        movieScript.setSubtitleText(13f, Color.WHITE, "- GDX-255, this is Interdimensional Control - request granted.");
        ActorScript hangarFloorScript = new ActorScript(hangarFloorId, hangarSceneStart, hangarSceneLength);
        movieScript.addActorScript(hangarFloorScript);
        ActorScript hangarWallScript = new ActorScript(hangarWallId, hangarSceneStart, hangarSceneLength);
        movieScript.addActorScript(hangarWallScript);


        return movieScript;
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;

        camera.position.set(8f, 1f, 8f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0, 0f);
        camera.update();

        return camera;
    }

    private GraphShaderModels createModels() {
        GraphShaderModels models = new GraphShaderModels();

        JsonReader jsonReader = new JsonReader();
        G3dModelLoader jsonModelLoader = new G3dModelLoader(jsonReader);

        Model luminarisModel = jsonModelLoader.loadModel(Gdx.files.internal("model/luminaris/luminaris.g3dj"));
        disposables.add(luminarisModel);
        luminarisId = models.registerModel(luminarisModel);
        models.addModelDefaultTag(luminarisId, "Default");
//
//        float shipScale = 0.008f;
//        GraphShaderModelInstance shipModelInstance = models.createModelInstance(luminarisId);
//        shipModelInstance.getTransformMatrix().scale(shipScale, shipScale, shipScale);

        ModelBuilder modelBuilder = new ModelBuilder();
        Model hangarFloor = modelBuilder.createRect(
                -10, 0, -10,
                -10, 0, 10,
                10, 0, 10,
                10, 0, -10,
                0, 1, 0,
                new Material(), VertexAttributes.Usage.Position);
        disposables.add(hangarFloor);
        hangarFloorId = models.registerModel(hangarFloor);
        models.addModelDefaultTag(hangarFloorId, "Hangar Floor");

        Model hangarWall = modelBuilder.createRect(
                -10, 20, -10,
                -10, 0, -10,
                10, 0, -10,
                10, 20, -10,
                0, 0, 1,
                new Material(), VertexAttributes.Usage.Position);
        disposables.add(hangarWall);
        hangarWallId = models.registerModel(hangarWall);
        models.addModelDefaultTag(hangarWallId, "Hangar Wall");

        return models;
    }

    private Label createStageSubtitleLabel() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        disposables.add(skin);

        stage = new Stage(new ScreenViewport());
        disposables.add(stage);

        Label label = new Label("", skin);
        label.setFontScale(2f);
        label.setAlignment(Align.center);
        label.setWrap(true);

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.bottom);

        tbl.add(label).width(Value.percentWidth(0.8f, tbl)).height(Value.percentHeight(0.2f, tbl));

        stage.addActor(tbl);
        return label;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        script.update(delta);
        stage.act(delta);

        pipelineRenderer.render(delta, RenderOutputs.drawToScreen);
    }


    @Override
    public void dispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }

        Gdx.app.debug("Unclosed", Cubemap.getManagedStatus());
        Gdx.app.debug("Unclosed", GLFrameBuffer.getManagedStatus());
        Gdx.app.debug("Unclosed", Mesh.getManagedStatus());
        Gdx.app.debug("Unclosed", Texture.getManagedStatus());
        Gdx.app.debug("Unclosed", ShaderProgram.getManagedStatus());
    }

    private PipelineRenderer loadPipelineRenderer(GraphShaderModels models, Camera camera, Stage stage) {
        try {
            InputStream stream = Gdx.files.internal("pipeline/demo.json").read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(stream, new PipelineLoaderCallback());
                pipelineRenderer.setPipelineProperty("Models", models);
                pipelineRenderer.setPipelineProperty("Camera", camera);
                pipelineRenderer.setPipelineProperty("Stage", stage);
                disposables.add(pipelineRenderer);
                return pipelineRenderer;
            } finally {
                stream.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Unable to load pipeline", exp);
        }
    }

}