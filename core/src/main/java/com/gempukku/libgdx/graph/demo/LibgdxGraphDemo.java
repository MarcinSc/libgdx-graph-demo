package com.gempukku.libgdx.graph.demo;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
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
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiFunction;

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
    private String planetGroundId;
    private Label pauseLabel;

    private boolean lastSpacePressed = false;
    private boolean paused = false;
    private String waterSurfaceId;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Label subtitleLabel = createStageSubtitleLabel();

        GraphShaderModels models = createModels();
        pipelineRenderer = loadPipelineRenderer(models, stage);

        script = createScript(subtitleLabel, models, pipelineRenderer);
        // TODO: Temp - move to planet scene
        script.update(42f);

        Gdx.input.setInputProcessor(stage);
    }

    private MovieScript createScript(Label subtitleLabel, GraphShaderModels models, PipelineRenderer pipelineRenderer) {
        MovieScript movieScript = new MovieScript(subtitleLabel, models, pipelineRenderer);
        // Introduction
        movieScript.setPipelineCamera(0f, "Camera", createDefaultCamera());
        movieScript.setSubtitleText(0f, Color.WHITE, "This is not a game.\nThis is a libGDX-Graph Demo.");
        movieScript.setSubtitleText(2.3f, Color.WHITE, "");
        movieScript.setSubtitleText(2.6f, Color.WHITE, "No shaders were written during creation of this demo.\n" +
                "All graphical assets were downloaded from cgtrader.com");
        movieScript.setSubtitleText(6.2f, Color.WHITE, "");
        movieScript.setSubtitleText(6.5f, Color.WHITE, "You may press SPACE to pause/unpause the movie.");
        movieScript.setSubtitleText(9.3f, Color.WHITE, "");
        movieScript.setSubtitleText(9.5f, new Color(0.8f, 0.8f, 1f, 1f), "A few centuries ago, in a far away galaxy...");

        // Hangar scene
        float hangarSceneStart = 10f;
        float hangarSceneLength = 33f;
        createHangarScene(movieScript, hangarSceneStart, hangarSceneLength);

        float planetSceneStart = 43f;
        float planetSceneLength = 60f;
        createPlanetScene(movieScript, planetSceneStart, planetSceneLength);

        return movieScript;
    }

    private void createPlanetScene(MovieScript movieScript, float planetSceneStart, float planetSceneLength) {
        movieScript.setPipelineCamera(planetSceneStart, "Camera", createPlanetSceneCamera());
        movieScript.setPipelineLights(planetSceneStart, "Lights", createPlanetSceneLights());
        movieScript.setPipelineFloatProperty("Blackout", planetSceneStart, 3f, 1, 0, Interpolation.pow3In);
        movieScript.setPipelineFloatProperty("Blur", planetSceneStart, 3f, BLUR_VALUE, 0);
        movieScript.setSubtitleText(planetSceneStart + 1f, new Color(0.8f, 0.8f, 1f, 1f), "Planet's surface in a parallel world");
        movieScript.setSubtitleText(planetSceneStart + 4f, Color.WHITE, "");
        movieScript.addActorScript(new ActorScript(planetGroundId, planetSceneStart, planetSceneLength));
        movieScript.addActorScript(new ActorScript(waterSurfaceId, planetSceneStart, planetSceneLength));
    }

    private void createHangarScene(MovieScript movieScript, float hangarSceneStart, float hangarSceneLength) {
        movieScript.setPipelineCamera(hangarSceneStart, "Camera", createHangarSceneCamera());
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

    private Camera createDefaultCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;
        camera.update();

        return camera;
    }

    private Camera createHangarSceneCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;

        camera.position.set(8f, 4f, 8f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 3f, 0f);
        camera.update();

        return camera;
    }

    private Camera createPlanetSceneCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;

        camera.position.set(8f, 1f, 8f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 1f);
        camera.update();

        return camera;
    }

    private GraphShaderEnvironment createPlanetSceneLights() {
        GraphShaderEnvironment environment = new GraphShaderEnvironment();
        environment.setAmbientColor(new Color(0.3f, 0.3f, 0.3f, 1f));
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(Color.WHITE);
        directionalLight.set(Color.WHITE, new Vector3(-1f, -1f, 0f));
        environment.addDirectionalLight(directionalLight);

        return environment;
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
        createHangarFloor(models, modelBuilder);
        createHangarWall(models, modelBuilder);
        createHangarShield(models, modelBuilder);
        createShipShield(models, modelBuilder);
        createPlanetGround(models, modelBuilder);
        createWaterSurface(models, modelBuilder);

        return models;
    }

    private void createWaterSurface(GraphShaderModels models, ModelBuilder modelBuilder) {
        modelBuilder.begin();
        MeshPartBuilder waterBuilder = modelBuilder.part("waterSurface", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material());
        createXZPlane(waterBuilder, new Vector2(2, 2), new Vector2(10, 10), 50, 50,
                new BiFunction<Float, Float, Float>() {
                    @Override
                    public Float apply(Float x, Float z) {
                        return -0.15f;
                    }
                });
        Model water = modelBuilder.end();
        disposables.add(water);
        waterSurfaceId = models.registerModel(water);
        models.addModelDefaultTag(waterSurfaceId, "Water Surface");
    }

    private void createPlanetGround(GraphShaderModels models, ModelBuilder modelBuilder) {
        modelBuilder.begin();
        MeshPartBuilder groundBuilder = modelBuilder.part("ground", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material());
        Vector2 tmp = new Vector2();
        Vector2 lakeCenter = new Vector2(6, 8);
        float lakeRadius = 3.5f;
        float lakeDepth = 1f;
        float lakeRadius2 = lakeRadius * lakeRadius;
        createXZPlane(groundBuilder, new Vector2(-10, -10), new Vector2(10, 10), 100, 100,
                new BiFunction<Float, Float, Float>() {
                    @Override
                    public Float apply(Float x, Float z) {
                        float distToCenter2 = tmp.set(x, z).dst2(lakeCenter);
                        if (distToCenter2 < lakeRadius2) {
                            return -lakeDepth + lakeDepth * distToCenter2 / lakeRadius2;
                        }
                        return 0f;
                    }
                });
        Model planetGround = modelBuilder.end();
        disposables.add(planetGround);
        planetGroundId = models.registerModel(planetGround);
        models.addModelDefaultTag(planetGroundId, "Planet Ground");
    }

    private void createXZPlane(MeshPartBuilder meshPartBuilder, Vector2 start, Vector2 end, int xSubdivisions, int zSubdivisions, BiFunction<Float, Float, Float> heightFunction) {
        float xStart = start.x;
        float xMultiplier = (end.x - start.x) / xSubdivisions;
        float zStart = start.y;
        float zMultiplier = (end.y - start.y) / zSubdivisions;

        Vector3[] posTemp = new Vector3[4];
        for (int i = 0; i < posTemp.length; i++) {
            posTemp[i] = new Vector3();
        }

        MeshPartBuilder.VertexInfo[] vertexInfo = new MeshPartBuilder.VertexInfo[6];
        for (int i = 0; i < vertexInfo.length; i++) {
            vertexInfo[i] = new MeshPartBuilder.VertexInfo();
        }

        Vector3[] normalTemp = new Vector3[2];
        for (int i = 0; i < normalTemp.length; i++) {
            normalTemp[i] = new Vector3();
        }

        for (int x = 0; x < xSubdivisions; x++) {
            for (int z = 0; z < zSubdivisions; z++) {
                float x1 = xStart + xMultiplier * x;
                float x2 = xStart + xMultiplier * (x + 1);
                float z1 = zStart + zMultiplier * z;
                float z2 = zStart + zMultiplier * (z + 1);

                posTemp[0].set(x1, heightFunction.apply(x1, z1), z1);
                posTemp[1].set(x1, heightFunction.apply(x1, z2), z2);
                posTemp[2].set(x2, heightFunction.apply(x2, z2), z2);
                posTemp[3].set(x2, heightFunction.apply(x2, z1), z1);

                // Vertices 0, 1, 2 and 2, 3, 0
                normalTemp[0].set(posTemp[1]).sub(posTemp[0]).crs(
                        posTemp[2].x - posTemp[1].x, posTemp[2].y - posTemp[1].y, posTemp[2].z - posTemp[1].z).nor();
                normalTemp[1].set(posTemp[3]).sub(posTemp[2]).crs(
                        posTemp[0].x - posTemp[3].x, posTemp[0].y - posTemp[3].y, posTemp[0].z - posTemp[3].z).nor();

                vertexInfo[0].set(posTemp[0], normalTemp[0], null, null);
                vertexInfo[1].set(posTemp[1], normalTemp[0], null, null);
                vertexInfo[2].set(posTemp[2], normalTemp[0], null, null);
                vertexInfo[3].set(posTemp[2], normalTemp[1], null, null);
                vertexInfo[4].set(posTemp[3], normalTemp[1], null, null);
                vertexInfo[5].set(posTemp[0], normalTemp[1], null, null);

                meshPartBuilder.triangle(vertexInfo[0], vertexInfo[1], vertexInfo[2]);
                meshPartBuilder.triangle(vertexInfo[3], vertexInfo[4], vertexInfo[5]);
            }
        }
    }

    private void createShipShield(GraphShaderModels models, ModelBuilder modelBuilder) {
        Model shipShield = modelBuilder.createSphere(18f, 6f, 12f, 50, 50, new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        disposables.add(shipShield);
        shipShieldId = models.registerModel(shipShield);
        models.addModelDefaultTag(shipShieldId, "Ship Shield");
    }

    private void createHangarShield(GraphShaderModels models, ModelBuilder modelBuilder) {
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
    }

    private void createHangarWall(GraphShaderModels models, ModelBuilder modelBuilder) {
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
    }

    private void createHangarFloor(GraphShaderModels models, ModelBuilder modelBuilder) {
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
    }

    private Label createStageSubtitleLabel() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        disposables.add(skin);

        stage = new Stage(new ScreenViewport());
        disposables.add(stage);

        pauseLabel = new Label("", skin);
        pauseLabel.setFontScale(2f);
        pauseLabel.setAlignment(Align.center);
        pauseLabel.setColor(Color.YELLOW);

        Label label = new Label("", skin);
        label.setFontScale(2f);
        label.setAlignment(Align.center);
        label.setWrap(true);

        Table tbl = new Table(skin);

        tbl.setFillParent(true);
        tbl.align(Align.bottom);

        tbl.add(pauseLabel).width(Value.percentWidth(0.8f, tbl)).height(Value.percentHeight(0.2f, tbl));
        tbl.row();
        tbl.add(label).width(Value.percentWidth(0.8f, tbl)).height(Value.percentHeight(0.2f, tbl));
        tbl.row();

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
        stage.act(delta);

        boolean spacePressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if (!lastSpacePressed && spacePressed) {
            paused = !paused;
            pauseLabel.setText(paused ? "Paused" : "");
            lastSpacePressed = true;
        }
        if (lastSpacePressed && !spacePressed) {
            lastSpacePressed = false;
        }
        if (!paused) {
            script.update(delta);
        }

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

    private PipelineRenderer loadPipelineRenderer(GraphShaderModels models, Stage stage) {
        try {
            InputStream stream = Gdx.files.internal("pipeline/demo.json").read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(stream, new PipelineLoaderCallback());
                pipelineRenderer.setPipelineProperty("Models", models);
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