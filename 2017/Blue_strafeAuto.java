//working code blue left
package CrownJoules2017;
//stop and reset encoders 
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import android.graphics.Color;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Locale;

@Autonomous

public class Blue_strafeAuto extends LinearOpMode
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    //
    private float hsvValues[] = {0F, 0F, 0F};
    private final int index=0;
    private float hueValue;
    //
   
    //public static final String TAG = "Vuforia VuMark Sample";
    OpenGLMatrix lastLocation = null;
    private DcMotor frontRight;  
    private DcMotor frontLeft;  
    private DcMotor backRight;  
    private DcMotor backLeft;  
    private BNO055IMU imu;
    private ColorSensor colorSensor;  
    private Servo topJewel;
    private Servo bottomJewel;
    private DcMotor vLiftMotor;
    private Servo leftClaw;  
    private Servo rightClaw;
    VuforiaLocalizer vuforia;
    private ElapsedTime runtime = new ElapsedTime();
    ModernRoboticsI2cRangeSensor rangeSensor;
    
    static final double COUNTS_PER_MOTOR_REV =1120; //NeveRest 40 motor gives 1120         counts per revolution of output shaft 
    static final double DRIVE_GEAR_REDUCTION = .66; //<1.0 if geared Up (big to small) output/input. input gear turns 1/2 for output to turn once.
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = ( COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    //encoder counts x gear reduction / circumference of wheel 
    static final double DRIVE_SPEED = .6;
    static final double TURN_SPEED=.5;
    
    int positionFL;
    int positionBL;
    int positionFR;
    int positionBR;
    
    int targetFL_Straight;
    int targetBL_Straight;
    int targetFR_Straight;
    int targetBR_Straight;
    
    int targetFL_L;
    int targetBL_L;
    int targetFR_L;
    int targetBR_L;
    
    int targetFL_R;
    int targetBL_R;
    int targetFR_R;
    int targetBR_R;
    
    int targetFL_C;
    int targetBL_C;
    int targetFR_C;
    int targetBR_C;
    
    int targetFL_push;
    int targetBL_push;
    int targetFR_push;
    int targetBR_push;
    
    int targetFL_backup;
    int targetBL_backup;
    int targetFR_backup;
    int targetBR_backup;
    
    int targetFL_repush;
    int targetBL_repush;
    int targetFR_repush;
    int targetBR_repush;
    
    int targetFL_rebackup;
    int targetBL_rebackup;
    int targetFR_rebackup;
    int targetBR_rebackup;
    
    boolean vuMark_Left = false;
    boolean vuMark_Right = false;
    boolean vuMark_Center = false;
    boolean unknown= true;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    //----------------------------------------------------------------------------------------------
    // Main logic
    //----------------------------------------------------------------------------------------------
    
    @Override public void runOpMode() {

        //vuforia 
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters V_parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        topJewel = hardwareMap.get(Servo.class,"top_Jewel");
        V_parameters.vuforiaLicenseKey = "Aafd4OT/////AAAAGaP7TsnlGkrcuLp33dxex75AR5RZKWEdvaWtsA/HuWW89X76KANsAg/yLnn2jY9gjuLAqWlZ6ZJaqI2BmtckzRZres9TNbHKeSEdhZPTpDl8OkmbvK9xcGG9dRdbKoyNWbhpQeM7J7xIlD9Fr3M9OVJJniFkxN2Bh3Vhi+CClnHVQZizsBen44gHa7elMGPHN8U9cJMI002LKa+GFeSXthJUCGQo/vsq8ChymwuVaI5y+b0UoR7710Wq7VnUi4wd0hj1yKb82fyOxDmDkjr8Pt2/V6UTWEvA5vIkzpxPyG8zPBszG4ILJGcwPRQ2yZL5lKho6SuUUMkFpU8Gu6KlL+z3/aYXb/fGNe+ba6KoG5At";
        //sets camera to front
        V_parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(V_parameters);
        
        //gyro
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        
        //hardware 
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        frontRight = hardwareMap.get(DcMotor.class, "front_Right");
        frontLeft  = hardwareMap.get(DcMotor.class, "front_Left"); 
        backLeft   = hardwareMap.get(DcMotor.class, "back_Left");
        backRight  = hardwareMap.get(DcMotor.class, "back_Right");
        colorSensor = hardwareMap.colorSensor.get("color_Sensor");
        bottomJewel = hardwareMap.get(Servo.class,"bottom_Jewel");
        topJewel = hardwareMap.get(Servo.class,"top_Jewel");
        vLiftMotor = hardwareMap.get(DcMotor.class, "vLift_Motor");
        vLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftClaw   = hardwareMap.get(Servo.class,"bottomLeft_Claw");  
        rightClaw  = hardwareMap.get(Servo.class,"bottomRight_Claw"); 
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "sensor_range");
        
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);  
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);  
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set up our telemetry dashboard
        range();
        
        // Wait until we're told to go
        waitForStart();
        
        positionFL= frontLeft.getCurrentPosition(); 
        positionBL= backLeft.getCurrentPosition();
        positionFR= frontRight.getCurrentPosition();
        positionBR= backRight.getCurrentPosition();
        
        targetBL_Straight= (positionBL + (int)(COUNTS_PER_INCH *22)*(-1));
        targetFL_Straight= (positionFL + (int)(COUNTS_PER_INCH *22)*(-1));//bc motors r reversed
        targetFR_Straight= (positionFR + (int)(COUNTS_PER_INCH *22)*(-1));
        targetBR_Straight= (positionBR + (int)(COUNTS_PER_INCH *22)*(-1));
        
        leftClaw.setPosition(.3); //new positions 1/20
        rightClaw.setPosition(.7);
            
        vLiftMotor.setPower(1);
        sleep(1000);
            
        vLiftMotor.setPower(0);
        
        newJewel();
        telemetry();
        sleep(3000);
       
        vuforia();
        
        toCrypto();
        
        //strafe right
        targetBL_L= (backLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *5)*(1));
        targetFL_L= (frontLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *5)*(-1));//bc motors r reversed
        targetFR_L= (frontRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *5)*(1));
        targetBR_L= (backRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *5)*(-1));
        
        targetBL_C= (backLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *16)*(1));
        targetFL_C= (frontLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *16)*(-1));//bc motors r reversed
        targetFR_C= (frontRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *16)*(1));
        targetBR_C= (backRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *16)*(-1));
        
        targetBL_R= (backLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *24)*(1));
        targetFL_R= (frontLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *24)*(-1));//bc motors r reversed
        targetFR_R= (frontRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *24)*(1));
        targetBR_R= (backRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *24)*(-1));
        
        strafe(); 
    
        targetBL_push= (backLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH * 11)*-1);
        targetFL_push= (frontLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *11)*-1);//bc motors r reversed
        targetFR_push= (frontRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *11)*-1);
        targetBR_push= (backRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *11)*-1);
        
        push();
        
        leftClaw.setPosition(.6); //new positions 1/20
        rightClaw.setPosition(.4);

        sleep(900);

        targetBL_backup=  backLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH * 7);
        targetFL_backup= frontLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *7);
        targetFR_backup= (frontRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *7));
        targetBR_backup= (backRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *7));
        
        backup();
 
        targetBL_repush= (backLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH * 6)*-1);
        targetFL_repush= (frontLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *6)*-1);//bc motors r reversed
        targetFR_repush= (frontRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *6)*-1);
        targetBR_repush= (backRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *6)*-1);
        
        leftClaw.setPosition(.3); //new positions 1/20
        rightClaw.setPosition(.7); //close claw
        
        sleep(900);
        
        vLiftMotor.setPower(-1);
        sleep(600);
            
        vLiftMotor.setPower(0);
        
        repush();
        
        targetBL_rebackup= backLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH * 6);
        targetFL_rebackup= frontLeft.getCurrentPosition() + (int)(COUNTS_PER_INCH *6);//bc motors r reversed
        targetFR_rebackup= frontRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *6);
        targetBR_rebackup= backRight.getCurrentPosition() + (int)(COUNTS_PER_INCH *6);
        
        rebackUp();
    }
    
    //----------------------------------------------------------------------------------------------
    //Methods
    //----------------------------------------------------------------------------------------------
    
    protected void range(){
        while (!(opModeIsActive())) {
            telemetry.addData("raw ultrasonic", rangeSensor.rawUltrasonic());
            telemetry.addData("cm", "%.2f cm", rangeSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
    }
    
    protected void newJewel() {
   
    /* colorSensor reads color as HSV (Hue, Saturation, Value) with the Hue value from 0 to 360. 
      Index 0 of array hsvValues returns the hue. The hue value determines if the color is red or blue. 
      If the sensor returns a hue value not within the range of the if statements below, the jewel 
      mechanism will return to its starting position and not hit a jewel. The point of using HSV is to
      set a range for the colors so that the jewel mechanism doesn't hit the wrong ball; this occurs
      when the robot is not positioned properly and the colorSensor reads the light which has a red value
      and returns that the color red is > than blue. */

    topJewel.setPosition(.685); //drop down arm lucas was here btw
    bottomJewel.setPosition(.5);
    sleep(3000);
    // convert the RGB values to HSV values.
    Color.RGBToHSV(colorSensor.red() * 255, colorSensor.green() * 255, colorSensor.blue() * 255, hsvValues);
    hueValue= hsvValues[index];
   
    //2 ranges for red hue: <15 or >320 
    if (hueValue< 15 || hueValue>320) //if red
    {  
      telemetry.addData("Color is: ", "Red");
      telemetry.addData("Red value:", hueValue);
      bottomJewel.setPosition(.8); //if red it swivels left and hits ball
      sleep(1500);
    } 
   
    //range for blue hue: between 200 and 250
    else if (hueValue> 150 && hueValue< 250) //if blue
    //else if (hueValue> 150 && hueValue< 200)//if blue
    {
      telemetry.addData("Color is: ", "Blue");
      telemetry.addData("Blue value:", hueValue);
      bottomJewel.setPosition(.2); //if red it swivels left and hits ball
      sleep(1500);
    }
    
    else {
        double x=.5;
        telemetry.addData("Idk ", "the color");
        telemetry.addData("Color value:", hueValue);
        // //while (! (( hueValue< 15 || hueValue>320 )||(hueValue> 150 && hueValue< 250)) )
        // //{
        //     for(int i=0; i<3; i++ )
        //     {
        //         bottomJewel.setPosition(x);
        //         x=x+.05;
        //         sleep(1000);
        //     }
        // //}
        
        sleep(1000);
    }
    
    topJewel.setPosition(.4);
    sleep(1000);
    bottomJewel.setPosition(.5);
    sleep(500);
    topJewel.setPosition(0);

} //end newJewel()   

protected void testJewel() {
   
   /* colorSensor reads color as HSV (Hue, Saturation, Value) with the Hue value from 0 to 360. 
      Index 0 of array hsvValues returns the hue. The hue value determines if the color is red or blue. 
      If the sensor returns a hue value not within the range of the if statements below, the jewel 
      mechanism will return to its starting position and not hit a jewel. The point of using HSV is to
      set a range for the colors so that the jewel mechanism doesn't hit the wrong ball; this occurs
      when the robot is not positioned properly and the colorSensor reads the light which has a red value
      and returns that the color red is > than blue. */
      

    topJewel.setPosition(.67); //drop down arm lucas was here btw

    sleep(3000);
    // convert the RGB values to HSV values.
    Color.RGBToHSV(colorSensor.red() * 255, colorSensor.green() * 255, colorSensor.blue() * 255, hsvValues);
    hueValue= hsvValues[index];
    
    //2 ranges for red hue: <15 or >320 
    if (hueValue< 15 || hueValue>320) //if red
    {  
      telemetry.addData("Color is: ", "Red");
      telemetry.addData("Red value:", hueValue);
      bottomJewel.setPosition(.8); //if red it swivels left and hits ball
      sleep(1500);
    } 
   
   //range for blue hue: between 200 and 250
   else if (hueValue> 150 && hueValue< 250) //if blue
   //else if (hueValue> 150 && hueValue< 200)//if blue
   {
      telemetry.addData("Color is: ", "Blue");
      telemetry.addData("Blue value:", hueValue);
      bottomJewel.setPosition(.2); //if red it swivels left and hits ball
      sleep(1500);
   }
    
    else {
        double x=.5;
        telemetry.addData("Looking for ", "the jewel...");
        telemetry.update();
        //if any of these are true, I want out of the loop. 
        while (! ((( hueValue< 15 || hueValue>320 ) || (hueValue> 150 && hueValue< 250)) ))
        {
            for(int i=0; i<3; i++ )
            {
                bottomJewel.setPosition(x);
                x=x+.02;
                sleep(1000);
                Color.RGBToHSV(colorSensor.red() * 255, colorSensor.green() * 255, colorSensor.blue() * 255, hsvValues);
                hueValue= hsvValues[index];
            }
        }//end while loop
        
        if (hueValue< 15 || hueValue>320) //if red
        {  
          telemetry.addData("Color is: ", "Red");
          telemetry.addData("Red value:", hueValue);
          bottomJewel.setPosition(.8); //if red it swivels left and hits ball
          sleep(1500);
        } 
       
       //range for blue hue: between 200 and 250
       else if (hueValue> 150 && hueValue< 250) //if blue
       //else if (hueValue> 150 && hueValue< 200)//if blue
       {
          telemetry.addData("Color is: ", "Blue");
          telemetry.addData("Blue value:", hueValue);
          bottomJewel.setPosition(.2); //if red it swivels left and hits ball
          sleep(1500);
       }
    }         
    
    topJewel.setPosition(.4);
    sleep(1000);
    bottomJewel.setPosition(.5);
    sleep(500);
    topJewel.setPosition(0);

} //end testJewel()


      protected void vuforia(){
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        
        boolean vuMarkFound=false;
        telemetry.addData("Looking", "...");
        telemetry.update();
         
        while (opModeIsActive() && (getRuntime() < 18.0) && (vuMarkFound==false)){
        relicTrackables.activate();
        
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        
            if (vuMark == RelicRecoveryVuMark.LEFT) {
            vuMark_Left = true;
            unknown=false;
            vuMarkFound=true;
            telemetry.addData("VuMark", "left");
            }

            else if (vuMark == RelicRecoveryVuMark.RIGHT) {
            vuMark_Right = true;
            unknown=false;
            vuMarkFound=true;
            telemetry.addData("VuMark", "right");
            }

            else if (vuMark == RelicRecoveryVuMark.CENTER) {
            vuMark_Center = true;
            unknown=false;
            vuMarkFound=true;
            telemetry.addData("VuMark", "center");
            }
        
        else {
            telemetry.addData("VuMark", "not visible");
            unknown=true;
        }
        telemetry.addData("VuMark", "%s visible", vuMark);
        telemetry.update();
        }//end OpmodeIsActive&Runtime<6
    } // end Vuforia
    
    protected void toCrypto(){
        frontLeft.setTargetPosition(targetFL_Straight);
        backLeft.setTargetPosition(targetBL_Straight);
        frontRight.setTargetPosition(targetFR_Straight);
        backRight.setTargetPosition(targetBR_Straight);
        
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       
        frontLeft.setPower(.2);
        backLeft.setPower(.2);
        frontRight.setPower(.2);
        backRight.setPower(.2);
        
        while(frontLeft.isBusy()){ //once @ position, stop robot
            idle();
        }
            
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }//end toCrypto()
    
    protected void strafe(){
        if(vuMark_Left == true){
            frontLeft.setTargetPosition(targetFL_L);
            backLeft.setTargetPosition(targetBL_L);
            frontRight.setTargetPosition(targetFR_L);
            backRight.setTargetPosition(targetBR_L);
        
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       
            frontLeft.setPower(.2);
            backLeft.setPower(.2);
            frontRight.setPower(.2);
            backRight.setPower(.2);
        
            while(frontLeft.isBusy()){ //once @ position, stop robot
                idle();
            }
            
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }//end if(vuMark_Left = true)
        
        else if (vuMark_Right == true){
            frontLeft.setTargetPosition(targetFL_R);
            backLeft.setTargetPosition(targetBL_R);
            frontRight.setTargetPosition(targetFR_R);
            backRight.setTargetPosition(targetBR_R);
        
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       
            frontLeft.setPower(.2);
            backLeft.setPower(.2);
            frontRight.setPower(.2);
            backRight.setPower(.2);
        
            while(frontLeft.isBusy()){ //once @ position, stop robot
                idle();
            }
            
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }//end if(vuMark_Right = true)
        
        else if(vuMark_Center == true){
            frontLeft.setTargetPosition(targetFL_C);
            backLeft.setTargetPosition(targetBL_C);
            frontRight.setTargetPosition(targetFR_C);
            backRight.setTargetPosition(targetBR_C);
        
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       
            frontLeft.setPower(.2);
            backLeft.setPower(.2);
            frontRight.setPower(.2);
            backRight.setPower(.2);
        
            while(frontLeft.isBusy()){ //once @ position, stop robot
                idle();
            }
            
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }//end if(vuMark_Center = true)
        
        else if (unknown == true){
            frontLeft.setTargetPosition(targetFL_C);
            backLeft.setTargetPosition(targetBL_C);
            frontRight.setTargetPosition(targetFR_C);
            backRight.setTargetPosition(targetBR_C);
        
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       
            frontLeft.setPower(.2);
            backLeft.setPower(.2);
            frontRight.setPower(.2);
            backRight.setPower(.2);
        
            while(frontLeft.isBusy()){ //once @ position, stop robot
                idle();
            }
            
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }
    } //end strafe()

    protected void backup(){
    
        frontLeft.setTargetPosition(targetFL_backup);
        backLeft.setTargetPosition(targetBL_backup);
        frontRight.setTargetPosition(targetFR_backup);
        backRight.setTargetPosition(targetBR_backup);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(.3);
        backLeft.setPower(.3);
        frontRight.setPower(.3);
        backRight.setPower(.3);
        
        while(frontLeft.isBusy()){
            idle();
        }
    
    frontLeft.setPower(0);
    backLeft.setPower(0);
    frontRight.setPower(0);
    backRight.setPower(0);
} //end backup

    protected void push(){
        frontLeft.setTargetPosition(targetFL_push);
        backLeft.setTargetPosition(targetBL_push);
        frontRight.setTargetPosition(targetFR_push);
        backRight.setTargetPosition(targetBR_push);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(.3);
        backLeft.setPower(.3);
        frontRight.setPower(.3);
        backRight.setPower(.3);
        
        while(frontLeft.isBusy()){
            idle();
        }
    
    frontLeft.setPower(0);
    backLeft.setPower(0);
    frontRight.setPower(0);
    backRight.setPower(0);

    } //end push
    
    protected void rebackUp(){
        frontLeft.setTargetPosition(targetFL_rebackup);
        backLeft.setTargetPosition(targetBL_rebackup);
        frontRight.setTargetPosition(targetFR_rebackup);
        backRight.setTargetPosition(targetBR_rebackup);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(.3);
        backLeft.setPower(.3);
        frontRight.setPower(.3);
        backRight.setPower(.3);
        
        while(frontLeft.isBusy()){
            idle();
        }
    
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    } // end rebackUp

    protected void repush(){
        frontLeft.setTargetPosition(targetFL_repush);
        backLeft.setTargetPosition(targetBL_repush);
        frontRight.setTargetPosition(targetFR_repush);
        backRight.setTargetPosition(targetBR_repush);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(.3);
        backLeft.setPower(.3);
        frontRight.setPower(.3);
        backRight.setPower(.3);
        
        while(frontLeft.isBusy()){
            idle();
        }
    
    frontLeft.setPower(0);
    backLeft.setPower(0);
    frontRight.setPower(0);
    backRight.setPower(0); 
    } //end repush
    

    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------
    void telemetry(){
        if (colorSensor.red() > colorSensor.blue()) 
            telemetry.addData("red: ", colorSensor.red()); 
        else if (colorSensor.red() < colorSensor.blue())
            telemetry.addData("blue:", colorSensor.blue());
            
        telemetry.update();
    }
   
   //only use this when gyro method is called, not for blue strafe  
   void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
                {
                // Acquiring the angles is relatively expensive; we don't want
                // to do that in each of the three items that need that info, as that's
                // three times the necessary expense.
                angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
                gravity  = imu.getGravity();
                }
            });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
    
}
