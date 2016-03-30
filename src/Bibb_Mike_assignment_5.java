/**
 * Created by kaigen on 3/11/16.
 */

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

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
          Data sqlData = new Data(argsInput);
          sqlData.getMetaData();
//        sqlData.getFace();
          Face drawFace = new Face(sqlData);
        drawFace.getPupils(dir);
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
 *      Face takes a @DATA object
 *      NOTE that the images are from the same vid that I broke apart in an earlier project
 *
 */
class Face {
    Data data;


        static int leftEye[] = new int[10];
        static int rightEye[] = new int[10];
        static int theNose[] = new int[10];
        static int theMouth[] = new int[10];
        static int theFace[] = new int[10];
        static int rightPupil[] = new int[10];
        static int leftPupil[] = new int[10];


    public Face(Data data) {
        this.data = data;
    }

    public void getPupils(String dir){
        String img = "";
        Mat image = Highgui.imread(dir);



    }

    private  void getFace(int num, int getMovieNumber)throws ClassNotFoundException, SQLException {
        String images = "";
        System.out.println("\nRunning FaceDetector");
        CascadeClassifier faceCascade = new CascadeClassifier(Bibb_Mike_assignment_5.class.getResource("haarcascade_frontalface_alt2.xml").getPath());
        Mat image = Highgui.imread(images);
        CascadeClassifier eyesCascade = new CascadeClassifier(Bibb_Mike_assignment_5.class.getResource("parojosG.xml").getPath());
        CascadeClassifier mouthCascade = new CascadeClassifier(Bibb_Mike_assignment_5.class.getResource("Mouth.xml").getPath());
        CascadeClassifier leftEyeCascade = new CascadeClassifier(Bibb_Mike_assignment_5.class.getResource("ojoI.xml").getPath());
        CascadeClassifier rightEyeCascade = new CascadeClassifier(Bibb_Mike_assignment_5.class.getResource("ojoD.xml").getPath());
        CascadeClassifier noseCascade = new CascadeClassifier(Bibb_Mike_assignment_5.class.getResource("Nariz_nuevo_20stages.xml").getPath());



        Mat mRgba = new Mat();
        Mat mGrey = new Mat();
        MatOfRect faces = new MatOfRect();
        MatOfRect mouth = new MatOfRect();
        MatOfRect eyes = new MatOfRect();
        MatOfRect lEye = new MatOfRect();
        MatOfRect rEye = new MatOfRect();
        MatOfRect aNose = new MatOfRect();

        image.copyTo(mRgba);
        image.copyTo(mGrey);
        Imgproc.cvtColor(mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(mGrey, mGrey);
        //faceCascade.detectMultiScale(mGrey, faces);



        faceCascade.detectMultiScale(mGrey, faces,1.1, 0,
                Objdetect.CASCADE_FIND_BIGGEST_OBJECT
                        | Objdetect.CASCADE_SCALE_IMAGE, new org.opencv.core.Size(30, 30), new org.opencv.core.Size());
        System.out.println(String.format("Detected %s face(s)", faces.toArray().length));

        //   faceCascade.detectMultiScale(mGrey, faces, 1.1, 5, 0, new Size(30, 30), new Size());
        Rect[] facesArray = faces.toArray();

        for (int i = 0; i < facesArray.length; i++) {
            Core.rectangle(mRgba, new Point(facesArray[i].x, facesArray[i].y), new Point(facesArray[i].x + facesArray[i].width, facesArray[i].y + facesArray[i].height),
                    new Scalar(0, 255, 0));

            // 0 = X
                /*
                0 = x
                1 = y
                2 = width
                3 = height
                 */

            theFace[0] = facesArray[i].x;
            theFace[1] = facesArray[i].y;
            theFace[2] = facesArray[i].width;
            theFace[3] = facesArray[i].height;



            mouthCascade.detectMultiScale(mGrey, mouth,1.1, 100,
                    Objdetect.CASCADE_FIND_BIGGEST_OBJECT
                            | Objdetect.CASCADE_SCALE_IMAGE, new org.opencv.core.Size(15, 15), new org.opencv.core.Size());
            Rect[] mouthsArray = mouth.toArray();

            leftEyeCascade.detectMultiScale(mGrey, lEye, 1.1, 40, 0, new Size(30, 30), new Size());
            Rect[] lEyeArray = lEye.toArray();

            rightEyeCascade.detectMultiScale(mGrey, rEye, 1.1, 40, 0, new Size(30, 30), new Size());
            Rect[] REyeArray = rEye.toArray();

            noseCascade.detectMultiScale(mGrey, aNose,1.1, 15,
                    Objdetect.CASCADE_FIND_BIGGEST_OBJECT
                            | Objdetect.CASCADE_SCALE_IMAGE, new org.opencv.core.Size(15, 15), new org.opencv.core.Size());
            Rect[] noseArray = aNose.toArray();

            for (int k = 0; k < mouthsArray.length; k++){
                if(k > 0) { break; }
                Core.rectangle(mRgba, new Point(mouthsArray[k].x, mouthsArray[k].y), new Point(mouthsArray[k].x + mouthsArray[k].width, mouthsArray[k].y + mouthsArray[k].height),
                        new Scalar(0, 255, 0));

                theMouth[0] = mouthsArray[i].x;
                theMouth[1] = mouthsArray[i].y;
                theMouth[2] = mouthsArray[i].width;
                theMouth[3] = mouthsArray[i].height;


            }
            for (int k = 0; k < lEyeArray.length; k++){
                if(k > 0) { break; }
                Core.rectangle(mRgba, new Point(lEyeArray[k].x, lEyeArray[k].y), new Point(lEyeArray[k].x + lEyeArray[k].width, lEyeArray[k].y + lEyeArray[k].height),
                        new Scalar(0, 255, 0));

                leftEye[0] = lEyeArray[i].x;
                leftEye[1] = lEyeArray[i].y;
                leftEye[2] = lEyeArray[i].width;
                leftEye[3] = lEyeArray[i].height;


            }
            for (int k = 0; k < REyeArray.length; k++){
                if(k > 0) { break; }
                Core.rectangle(mRgba, new Point(REyeArray[k].x, REyeArray[k].y), new Point(REyeArray[k].x + REyeArray[k].width, REyeArray[k].y + REyeArray[k].height),
                        new Scalar(0, 255, 0));
//
                rightEye[0] = REyeArray[i].x;
                rightEye[1] = REyeArray[i].y;
                rightEye[2] = REyeArray[i].width;
                rightEye[3] = REyeArray[i].height;


            }
//
            for (int k = 0; k < noseArray.length; k++){
                if(k > 0) { break; }
                Core.rectangle(mRgba, new Point(noseArray[k].x, noseArray[k].y), new Point(noseArray[k].x + noseArray[k].width, noseArray[k].y + noseArray[k].height),
                        new Scalar(0, 255, 0));

                theNose[0] = noseArray[i].x;
                theNose[1] = noseArray[i].y;
                theNose[2] = noseArray[i].width;
                theNose[3] = noseArray[i].height;

            }


        }


        String sql = "INSERT INTO BOUNDINGBOX (videonumber,frame,widthface,heightface,wholeface,lefteye,heightlefteye,widthlefteye,righteye,heightrighteye," +
                "widthrighteye,nose,widthnose,heightnose,mouth,widthmouth,heightmouth) "
                + "VALUES ("+ num + "," + getMovieNumber + "," + facesArray[0].width + "," + facesArray[0].height + "," + "point(" + facesArray[0].x + "," + facesArray[0].y+ ")"    + "," + "point(" + leftEye[0] + "," + leftEye[1] + ")" + "," + leftEye[3] + "," +leftEye[2] + "," +"point(" + rightEye[0] + "," + rightEye[1] + ")" + "," + rightEye[3] + "," + rightEye[2] + "," + "point(" +  theNose[0] + "," +  theNose[1] + ")" + "," +  theNose[2] + "," +  theNose[3] + "," + "point(" + theMouth[0] + "," + theMouth[1] + ")" + "," + theMouth[2] + "," + theMouth[3] + ");";


        Connection c = null;
        Statement stmt = null;

        Class.forName("org.postgresql.Driver");

        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cs161",
                "postgres", "postgres");
        c.setAutoCommit(false);
        System.out.println("Opened database successfully");
        stmt = c.createStatement();
        stmt.executeUpdate(sql);

        String filename = "ouput.png";
        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, mRgba);

        c.commit();
        stmt.close();
        c.close();
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

