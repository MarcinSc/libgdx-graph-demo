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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
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
    private static final float BLUR_VALUE = 30f;

    private Array<Disposable> disposables = new Array<>();

    private PipelineRenderer pipelineRenderer;
    private Stage stage;
    private Script script;
    private String hangarFloorId;
    private String hangarWallId;
    private String hangarShieldId;
    private String luminarisId;
    private String scifiPedestalId;
    private String crateId;
    private String cellId;
    private String shipShieldId;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Label subtitleLabel = createStageSubtitleLabel();

        Camera camera = createCamera();
        GraphShaderModels models = createModels();
        pipelineRenderer = loadPipelineRenderer(models, camera, stage);

        script = createScript(subtitleLabel, camera, models, pipelineRenderer);
        // TODO: Temp - move to hangar scene
        //script.update(30f);

        Gdx.input.setInputProcessor(stage);
    }

    private MovieScript createScript(Label subtitleLabel, Camera camera, GraphShaderModels models, PipelineRenderer pipelineRenderer) {
        MovieScript movieScript = new MovieScript(subtitleLabel, camera, models, pipelineRenderer);
        // Introduction
        movieScript.setSubtitleText(0f, Color.WHITE, "This is not a game.\nThis is a libGDX-Graph Demo.\n" +
                "No shaders were written during creation of this demo.\n" +
                "All graphical assets were free and not created by me.");
        movieScript.setSubtitleText(5f, Color.WHITE, "");
        movieScript.setSubtitleText(5.5f, new Color(0.8f, 0.8f, 1f, 1f), "A few centuries ago, in a far away galaxy...");

        // Hangar scene
        float hangarSceneStart = 6f;
        float hangarSceneLength = 33f;
        createHangarScene(movieScript, hangarSceneStart, hangarSceneLength);

        return movieScript;
    }

    private void createHangarScene(MovieScript movieScript, float hangarSceneStart, float hangarSceneLength) {
        movieScript.setPipelineFloatProperty("Blackout", hangarSceneStart, 3f, 1, 0, Interpolation.pow3In);
        movieScript.setPipelineFloatProperty("Blur", hangarSceneStart, 3f, BLUR_VALUE, 0);
        movieScript.setSubtitleText(hangarSceneStart + 3f, Color.WHITE, "");
        movieScript.setSubtitleText(hangarSceneStart + 3.5f, Color.WHITE, "- This is GDX-255 requesting permission to enter the transfer bay.");
        movieScript.setSubtitleText(hangarSceneStart + 7f, Color.WHITE, "- GDX-255, this is Interdimensional Control - request granted.");
        setupHangarEnvironment(movieScript, hangarSceneStart, hangarSceneLength);

        float luminarisScale = 0.22f;
        float luminarisStartingX = -50f;
        float luminarisY = 1.6f;
        float luminarisStartY = 4f;
        ActorScript luminarisActor = new ActorScript(luminarisId, hangarSceneStart, hangarSceneLength)
                .setScale(0, hangarSceneLength, luminarisScale, luminarisScale)
                .setRotation(0, hangarSceneLength, new Vector3(0, 1, 0), 90, 90)
                .setPosition(0, 8f, new Vector3(luminarisStartingX, luminarisY + luminarisStartY, 0), new Vector3(luminarisStartingX, luminarisY + luminarisStartY, 0))
                .setPosition(8f, 5f, new Vector3(luminarisStartingX, luminarisY + luminarisStartY, 0), new Vector3(0f, luminarisY, 0f), Interpolation.smooth)
                .removeTag(26f, "Default")
                .addTag(26f, "Dissolve")
                .setFloatProperty("Dissolve Strength", 26f, 5f, -0.2f, 1f);
        movieScript.addActorScript(luminarisActor);
        movieScript.setSubtitleText(hangarSceneStart + 12f, Color.WHITE, "");
        movieScript.setSubtitleText(hangarSceneStart + 14, Color.WHITE, "- GDX-255, raise your shields and prepare for interdimensional transfer.");
        Vector3 shipShieldPosition = new Vector3(0f, 2f, 0);
        movieScript.addActorScript(
                new ActorScript(shipShieldId, hangarSceneStart + 17f, 13.5f)
                        .setPosition(0, 0, shipShieldPosition, shipShieldPosition)
                        .setFloatProperty("Min Y", 0f, 5f, -1f, 8f));
        movieScript.setSubtitleText(hangarSceneStart + 19f, Color.WHITE, "");
        movieScript.setSubtitleText(hangarSceneStart + 22f, Color.WHITE, "- Shields raised, and we're ready for transfer.");
        movieScript.setSubtitleText(hangarSceneStart + 24.7f, Color.WHITE, "");
        movieScript.setSubtitleText(hangarSceneStart + 25f, Color.WHITE, "- Affirmative. Initiating interdimensional transfer.");
        movieScript.setSubtitleText(hangarSceneStart + 31f, Color.WHITE, "");
        movieScript.setPipelineFloatProperty("Blackout", hangarSceneStart + 31f, 2f, 0, 1, Interpolation.pow3In);
        movieScript.setPipelineFloatProperty("Blur", hangarSceneStart + 31f, 2f, 0, BLUR_VALUE);
    }

    private void setupHangarEnvironment(MovieScript movieScript, float hangarSceneStart, float hangarSceneLength) {
        movieScript.addActorScript(new ActorScript(hangarFloorId, hangarSceneStart, hangarSceneLength));
        movieScript.addActorScript(new ActorScript(hangarWallId, hangarSceneStart, hangarSceneLength));
        movieScript.addActorScript(new ActorScript(hangarShieldId, hangarSceneStart, hangarSceneLength));
        movieScript.addActorScript(
                new ActorScript(scifiPedestalId, hangarSceneStart, hangarSceneLength)
                        .setScale(0, hangarSceneLength, 0.002f, 0.002f));
        for (int i = 0; i < 5; i++) {
            Vector3 createPosition1 = new Vector3(-3 + i, 0, -9);
            Vector3 createPosition2 = new Vector3(-3 + i, 0, -8);
            movieScript.addActorScript(
                    new ActorScript(crateId, hangarSceneStart, hangarSceneLength)
                            .setScale(0, hangarSceneLength, 0.8f, 0.8f)
                            .setPosition(0, hangarSceneLength, createPosition1, createPosition1));
            movieScript.addActorScript(
                    new ActorScript(crateId, hangarSceneStart, hangarSceneLength)
                            .setScale(0, hangarSceneLength, 0.8f, 0.8f)
                            .setPosition(0, hangarSceneLength, createPosition2, createPosition2));
        }
        for (int i = 0; i < 6; i++) {
            Vector3 cellPosition = new Vector3(3 + i * 0.4f, 0.4f, -9);
            movieScript.addActorScript(
                    new ActorScript(cellId, hangarSceneStart, hangarSceneLength)
                            .setScale(0, hangarSceneLength, 0.002f, 0.002f)
                            .setPosition(0, hangarSceneLength, cellPosition, cellPosition));
        }
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;

        camera.position.set(8f, 4f, 8f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 3f, 0f);
        camera.update();

        return camera;
    }

    private GraphShaderModels createModels() {
        GraphShaderModels models = new GraphShaderModels();
        disposables.add(models);

        JsonReader jsonReader = new JsonReader();
        G3dModelLoader jsonModelLoader = new G3dModelLoader(jsonReader);
        UBJsonReader binaryReader = new UBJsonReader();
        G3dModelLoader binaryModelLoader = new G3dModelLoader(binaryReader);

        Model luminarisModel = jsonModelLoader.loadModel(Gdx.files.internal("model/luminaris/luminaris.g3dj"));
        disposables.add(luminarisModel);
        luminarisId = models.registerModel(luminarisModel);
        models.addModelDefaultTag(luminarisId, "Default");

        Model scifiPedestalModel = jsonModelLoader.loadModel(Gdx.files.internal("model/scifi-pedestal/tech_pedestal.g3dj"));
        disposables.add(scifiPedestalModel);
        scifiPedestalId = models.registerModel(scifiPedestalModel);
        models.addModelDefaultTag(scifiPedestalId, "Default");

        Model crateModel = jsonModelLoader.loadModel(Gdx.files.internal("model/crate/crate.g3dj"));
        disposables.add(crateModel);
        crateId = models.registerModel(crateModel);
        models.addModelDefaultTag(crateId, "Default");

        Model cellModel = jsonModelLoader.loadModel(Gdx.files.internal("model/cell/cell.g3dj"));
        disposables.add(cellModel);
        cellId = models.registerModel(cellModel);
        models.addModelDefaultTag(cellId, "Default");

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

        Model hangarShield = modelBuilder.createRect(
                -10, 20, 10,
                -10, 0, 10,
                -10, 0, -10,
                -10, 20, -10,
                0, 0, 1,
                new Material(), VertexAttributes.Usage.Position);
        disposables.add(hangarShield);
        hangarShieldId = models.registerModel(hangarShield);
        models.addModelDefaultTag(hangarShieldId, "Hangar Shield");

        Model shipShield = modelBuilder.createSphere(18f, 6f, 12f, 50, 50, new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        disposables.add(shipShield);
        shipShieldId = models.registerModel(shipShield);
        models.addModelDefaultTag(shipShieldId, "Ship Shield");

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