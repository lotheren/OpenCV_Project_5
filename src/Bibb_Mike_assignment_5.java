/**
 * Created by kaigen on 3/11/16.
 */

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;


public class Bibb_Mike_assignment_5 {
    /** @dir this is the directory where the images are that are to be drawn on
     */
    public static String dir = "video_repository_39/testvid.movout%d.png";



    public static void main(String args[]) {
        String movieLocation = "outputImages";
        String makeMovieDir = "movieStorage";

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int argsInput = Integer.parseInt(args[0]);
        /**
         * This is my old command line run that wa used in project 4.
         * I am trying to build one large program that **Eventually*** I will be be able to use
         * to do this whole entire project from 2 - END.
         */
//
//        Data sqlData = new Data(argsInput);
//        sqlData.getMetaData();
//        sqlData.getFace();
//        DrawFace draw = new DrawFace(sqlData);
//        draw.draw(dir);
//        FFmpeg movieMaker = new FFmpeg(movieLocation, makeMovieDir, sqlData);
//        movieMaker.createDir();
//        movieMaker.createMovie();


    }

}

/**
 * Data Does 2 things.
 * 1) It connects the the database
 * 2) Because I am new to using a database this may be wrong... But I am
 *     storing the values that  I need inside arrays and such. Maybe I do not need to do this
 *     I will investigate on a later time.
 */


class Data {

    private int vidNumber, frames, xWidth, yWidth;
    private String rEye, lEye, nose, mouth, face;
    ArrayList<Integer> faceX, faceY, rEyeX, rEyeY, lEyeX, lEyeY, noseX, noseY, mouthX, mouthY,
            wFace, hFace, wREye, hREye, wLEye, hLEye, wNose, hNose, wMouth, hMouth;
    private double fps;
    private final static String sqlConnect = "jdbc:postgresql://localhost:5432/cs161";
    private final static String databaseUsrName = "postgres";
    private final static String dataBasePassword = "postgres";


    public String getWholeFace() {
        return face;
    }

    public String getrEye() {
        return rEye;
    }

    public String getlEye() {
        return lEye;
    }

    public String getNose() {
        return nose;
    }

    public String getMouth() {
        return mouth;
    }


    public int getVidNumber() {
        return vidNumber;
    }

    public int getFrames() {
        return frames;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public int getxWidth() {
        return xWidth;
    }

    public void setxWidth(int xWidth) {
        this.xWidth = xWidth;
    }

    public int getyWidth() {
        return yWidth;
    }

    public void setyWidth(int yWidth) {
        this.yWidth = yWidth;
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    public Data(int vidNumber) {
        this.vidNumber = vidNumber;
        this.faceX = new ArrayList<>();
        this.faceY = new ArrayList<>();
        this.rEyeX = new ArrayList<>();
        this.rEyeY = new ArrayList<>();
        this.lEyeX = new ArrayList<>();
        this.lEyeY = new ArrayList<>();
        this.noseX = new ArrayList<>();
        this.noseY = new ArrayList<>();
        this.mouthX = new ArrayList<>();
        this.mouthY = new ArrayList<>();
        this.hMouth = new ArrayList<>();
        this.wMouth = new ArrayList<>();
        this.wNose = new ArrayList<>();
        this.hNose = new ArrayList<>();
        this.wFace = new ArrayList<>();
        this.hFace = new ArrayList<>();
        this.hREye = new ArrayList<>();
        this.wREye = new ArrayList<>();
        this.wLEye = new ArrayList<>();
        this.hLEye = new ArrayList<>();


    }

    /*
    Gets the metaData from the Database
     */
    public void getMetaData() {

        try {
            Connection c = null;
            Statement stmt = null;

            Class.forName("org.postgresql.Driver");

            c = DriverManager.getConnection(sqlConnect, databaseUsrName, dataBasePassword);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM METADATA;");

            while (rs.next()) {
                int id = rs.getInt("videonumber");
                if (id != vidNumber) {
                    continue;
                }
                frames = rs.getInt("numofframes");
                xWidth = rs.getInt("xwidth");
                yWidth = rs.getInt("ywidth");
                fps = rs.getDouble("fps");

                System.out.println("Metadata has been received.");
                System.out.println("ID = " + id);
                System.out.println("FRAMES = " + frames);
                System.out.println("xWidth = " + xWidth);
                System.out.println("xWidth = " + yWidth);
                System.out.println("FPS = " + fps);
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("OOOOPPPSSSS");
        }
    }

    /*
     gets all the face dataPoints.
     TODO finish up getting points
     */

    public void getFace() {
        try {
            Connection c = null;
            Statement stmt = null;

            Class.forName("org.postgresql.Driver");

            c = DriverManager.getConnection(sqlConnect, databaseUsrName, dataBasePassword);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM BOUNDINGBOX;");

            while (rs.next()) {
                int id = rs.getInt("videonumber");
                int vidID = rs.getInt("videoID");
                if (id != vidNumber) {
                    continue;
                }
                face = rs.getObject("WholeFace").toString();
                faceX.add(Integer.parseInt(face.substring(1, 4)));
                faceY.add(Integer.parseInt(face.substring(7, 10)));
                rEye = rs.getObject("righteye").toString();
                rEyeX.add(Integer.parseInt(rEye.substring(1, 4)));
                rEyeY.add(Integer.parseInt(rEye.substring(7, 10)));
                lEye = rs.getObject("lefteye").toString();
                lEyeX.add(Integer.parseInt(lEye.substring(1, 4)));
                lEyeY.add(Integer.parseInt(lEye.substring(7, 10)));
                nose = rs.getObject("nose").toString();
                noseX.add(Integer.parseInt(nose.substring(1, 4)));
                noseY.add(Integer.parseInt(nose.substring(7, 10)));
                mouth = rs.getObject("mouth").toString();
                mouthX.add(Integer.parseInt(mouth.substring(1, 4)));
                mouthY.add(Integer.parseInt(mouth.substring(7, 10)));

                wFace.add(rs.getInt("widthface"));
                hFace.add(rs.getInt("heightface"));

                wREye.add(rs.getInt("widthrighteye"));
                hREye.add(rs.getInt("heightrighteye"));

                wLEye.add(rs.getInt("widthlefteye"));
                hLEye.add(rs.getInt("heightlefteye"));

                wNose.add(rs.getInt("widthnose"));
                hNose.add(rs.getInt("heightnose"));

                wMouth.add(rs.getInt("widthmouth"));
                hMouth.add(rs.getInt("heightmouth"));


            }

            rs.close();
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

}

/**
 * Draw face....
 *      Draw face takes the info that I grabbed from the database and draws
 *      bounding boxes on to the images that are in a directory I specified.
 *      NOTE that the images are from the same vid that I broke apart in an earlier project
 *
 */
class DrawFace {
    Data data;


    public DrawFace(Data data) {
        this.data = data;

    }

    public void draw(String dir) {
        String images = "";
        String outputImage = "";
        for (int i = 0; i < data.getFrames(); i++) {
            // loading the image to be drawn on
            images = String.format(dir, i + 1);
            Mat image = Highgui.imread(images);
            Mat mRgba = new Mat();
            image.copyTo(mRgba);
            Core.rectangle(mRgba, new Point(data.faceX.get(i), data.faceY.get(i)), new Point(data.faceX.get(i) + data.wFace.get(i), data.faceY.get(i) + data.hFace.get(i)),
                    new Scalar(255, 0, 0));
            Core.rectangle(mRgba, new Point(data.mouthX.get(i), data.mouthY.get(i)), new Point(data.mouthX.get(i) + data.wMouth.get(i), data.mouthY.get(i) + data.hMouth.get(i)),
                    new Scalar(255, 0, 0));
            Core.rectangle(mRgba, new Point(data.noseX.get(i), data.noseY.get(i)), new Point(data.noseX.get(i) + data.wNose.get(i), data.noseY.get(i) + data.hMouth.get(i)),
                    new Scalar(255, 0, 0));
            Core.rectangle(mRgba, new Point(data.rEyeX.get(i), data.rEyeY.get(i)), new Point(data.rEyeX.get(i) + data.wREye.get(i), data.rEyeY.get(i) + data.hREye.get(i)),
                    new Scalar(255, 0, 0));
            Core.rectangle(mRgba, new Point(data.lEyeX.get(i), data.lEyeY.get(i)), new Point(data.lEyeX.get(i) + data.wLEye.get(i), data.lEyeY.get(i) + data.hLEye.get(i)),
                    new Scalar(255, 0, 0));

            // Creates the directory to hold the images AND writes the images there
            try {
                String directory = "outputImages";
                Files.createDirectories(Paths.get(directory));
                outputImage = String.format("%s/movout%d.png", directory, i + 1);

                File source = new File(directory);
                File dest = new File("output%d.png" + 5);
                //  Files.copy(source.toPath(), dest.toPath());
                String filename = "ouput1.png";
                System.out.println(String.format("Writing %s", outputImage));
                Highgui.imwrite(outputImage, mRgba);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}

/**
 * @FFmpeg This class does all the console FFmpeg stuff.
 *          right now I just have it combine all the images in a dir
 *          back into a movie.
 *          !!The images now have a bounding box drawn on them.
 *          TODO create more functions that handle everything ffmpeg does and I need for this entire project series.
 */

class FFmpeg {

    public FFmpeg(String videoLocation, String videoDestination, Data data) {
        this.videoLocation = videoLocation;
        this.videoDestination = videoDestination;
        this.data = data;
    }

    Data data;
    String videoLocation;
    String videoDestination;

    //Create the dir to hold the movie @videoDestination
    public void createDir() {
        try {
            Files.createDirectories(Paths.get(videoDestination));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Creates the movie based off the frames that are inside @videoLocation
    public void createMovie() {
        try {
            String command;
            Process proc;
            command = "ffmpeg -r " + data.getFps() + " -start_number 1 -f image2 -i outputImages//movout%d.png -c:v libx264 " + videoDestination + "//bounding_box_movie_" + data.getVidNumber() + ".mp4";
            proc = Runtime.getRuntime().exec(command);
            proc.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

