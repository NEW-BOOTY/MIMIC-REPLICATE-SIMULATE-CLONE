// Copyright © 2024 Devin B. Royal. All Rights Reserved.

import javax.vecmath.*;

public class Imaginarium3DClient extends JFrame {
    private TransformGroup transformGroup;
    private Transform3D transform3D;
    private float x = 0.0f;
    private float y = 0.0f;
    private float z = -5.0f;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Imaginarium3DClient(String serverAddress) {
        try {
            // Set up the main game window
            setTitle("Imaginarium 3D Client");
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
                    try {
                        if (socket != null && !socket.isClosed()) {
                            socket.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.exit(0);
                }
            });

            // Add key listener for movement
            canvas3D.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
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
                    updateTransform();
                    sendPosition();
                }
            });

            // Request focus for the canvas to receive key events
            canvas3D.setFocusable(true);
            canvas3D.requestFocus();

            // Connect to the server
            try {
                socket = new Socket(serverAddress, 12345);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                new Thread(() -> {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            System.out.println("Server: " + message);
                            // Handle incoming messages (e.g., update other players' positions)
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTransform() {
        try {
            transform3D.setTranslation(new Vector3f(x, y, z));
            transformGroup.setTransform(transform3D);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPosition() {
        try {
            if (out != null) {
                out.println("POSITION " + x + " " + y + " " + z);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Create and display the game window
        SwingUtilities.invokeLater(() -> {
            try {
                String serverAddress = JOptionPane.showInputDialog("Enter server address:");
                Imaginarium3DClient game = new Imaginarium3DClient(serverAddress);
                game.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}