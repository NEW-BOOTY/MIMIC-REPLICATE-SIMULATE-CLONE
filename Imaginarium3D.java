// Copyright © 2024 Devin B. Royal. All Rights Reserved.

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.vecmath.*;

public class Imaginarium3D extends JFrame {

    private TransformGroup transformGroup;
    private Transform3D transform3D;
    private float x = 0.0f;
    private float y = 0.0f;
    private float z = -5.0f;
    private BoundingSphere sphereBounds;
    private BoundingBox obstacleBounds;

    public Imaginarium3D() {
        // Set up the main game window
        setTitle("Imaginarium 3D");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a Canvas3D and add it to the frame
        Canvas3D canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        add(canvas3D);

        // Create a SimpleUniverse and a BranchGroup
        SimpleUniverse universe = new SimpleUniverse(canvas3D);
        BranchGroup group = new BranchGroup();

        // Create a sphere and add it to the group
        Sphere sphere = new Sphere(0.1f);
        transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformGroup.addChild(sphere);
        group.addChild(transformGroup);

        // Initialize the transform
        transform3D = new Transform3D();
        updateTransform();

        // Define bounding volumes
        sphereBounds = new BoundingSphere(new Point3d(x, y, z), 0.1);

        // Create an obstacle (another sphere) and add it to the group
        Sphere obstacle = new Sphere(0.1f);
        TransformGroup obstacleTransformGroup = new TransformGroup();
        Transform3D obstacleTransform3D = new Transform3D();
        obstacleTransform3D.setTranslation(new Vector3f(0.5f, 0.0f, -5.0f));
        obstacleTransformGroup.setTransform(obstacleTransform3D);
        obstacleTransformGroup.addChild(obstacle);
        group.addChild(obstacleTransformGroup);

        // Define bounding volume for the obstacle
        obstacleBounds = new BoundingBox(new Point3d(0.4, -0.1, -5.1), new Point3d(0.6, 0.1, -4.9));

        // Add light to the scene
        Color3f lightColor = new Color3f(1.0f, 1.0f, 1.0f);
        BoundingSphere lightBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        DirectionalLight light = new DirectionalLight(lightColor, new Vector3f(4.0f, -7.0f, -12.0f));
        light.setInfluencingBounds(lightBounds);
        group.addChild(light);

        // Add background to the scene
        Background background = new Background(new Color3f(0.0f, 0.0f, 0.0f));
        background.setApplicationBounds(lightBounds);
        group.addChild(background);

        // Compile the group and add it to the universe
        group.compile();
        universe.addBranchGraph(group);

        // Handle window closing events
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Add key listener for movement
        canvas3D.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                float oldX = x;
                float oldY = y;
                float oldZ = z;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        y += 0.1f;
                        break;
                    case KeyEvent.VK_S:
                        y -= 0.1f;
                        break;
                    case KeyEvent.VK_A:
                        x -= 0.1f;
                        break;
                    case KeyEvent.VK_D:
                        x += 0.1f;
                        break;
                    case KeyEvent.VK_Q:
                        z += 0.1f;
                        break;
                    case KeyEvent.VK_E:
                        z -= 0.1f;
                        break;
                }

                // Update bounding volume for the sphere
                sphereBounds.setCenter(new Point3d(x, y, z));

                // Check for collision
                if (sphereBounds.intersect(obstacleBounds)) {
                    // Revert to old position if collision detected
                    x = oldX;
                    y = oldY;
                    z = oldZ;
                }

                updateTransform();
            }
        });

        // Request focus for the canvas to receive key events
        canvas3D.setFocusable(true);
        canvas3D.requestFocus();
    }

    private void updateTransform() {
        transform3D.setTranslation(new Vector3f(x, y, z));
        transformGroup.setTransform(transform3D);
    }

    public static void main(String[] args) {
        // Create and display the game window
        SwingUtilities.invokeLater(() -> {
            Imaginarium3D game = new Imaginarium3D();
            game.setVisible(true);
        });
    }
}