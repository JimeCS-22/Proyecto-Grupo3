package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.trees.BTreeNode;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;

import java.util.List;

public class TreeAnimationManager {

    private final TreeRenderer renderer;

    private final Canvas canvas;

    private final Runnable redrawAction;

    public TreeAnimationManager(
            TreeRenderer renderer,
            Canvas canvas,
            Runnable redrawAction
    ) {

        this.renderer = renderer;

        this.canvas = canvas;

        this.redrawAction = redrawAction;
    }

    public void animateNodes(
            List<BTreeNode<Course>> nodes){

        Timeline timeline =
                new Timeline();

        for(int i = 0;
            i < nodes.size();
            i++){

            BTreeNode<Course> node =
                    nodes.get(i);

            timeline.getKeyFrames().add(

                    new KeyFrame(

                            Duration.seconds(
                                    i * 1.0
                            ),

                            e -> {

                                renderer.setHighlightedNode(node);

                                redrawAction.run();
                            }
                    )
            );
        }

        timeline.getKeyFrames().add(

                new KeyFrame(

                        Duration.seconds(
                                nodes.size() + 2
                        ),

                        e -> {

                            renderer.setHighlightedNode(null);

                            redrawAction.run();
                        }
                )
        );
        timeline.play();
    }

    public void animateSearchPath(
            List<BTreeNode<Course>> path){

        Timeline timeline =
                new Timeline();

        for(int i = 0;
            i < path.size();
            i++){

            BTreeNode<Course> node =
                    path.get(i);

            timeline.getKeyFrames().add(

                    new KeyFrame(

                            Duration.seconds(
                                    i * 0.8
                            ),

                            e -> {

                                renderer.setHighlightedNode(
                                        node
                                );

                                redrawAction.run();
                            }
                    )
            );
        }

        if(!path.isEmpty()){

            BTreeNode<Course> found =
                    path.get(path.size()-1);

            timeline.getKeyFrames().add(

                    new KeyFrame(

                            Duration.seconds(
                                    path.size() * 0.8
                            ),

                            e -> {

                                renderer.setHighlightedNode(
                                        null
                                );

                                renderer.setFoundNode(
                                        found
                                );

                                redrawAction.run();
                            }
                    )
            );
        }

        timeline.play();
    }
}
