package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class SAuto extends LinearOpMode {

    public DcMotor leftMotor;
    public DcMotor rightMotor;

    public Servo intakePivotServo;
    public float intakePivotServoPos = 0.5f; //What the servo above's current pos is; Sets to this at start
    public float intakePivotSensitivity = 500;

    public CRServo intakeServo1;
    public CRServo intakeServo2;
    
    int positionLM;
    int positionRM;
    int targetLM;
    int targetRM;

    final double COUNTS_PER_MOTOR_REV =1120; //NeveRest 40 motor gives 1120 counts per revolution of output shaft
    final double DRIVE_GEAR_REDUCTION = 1; //<1.0 if geared Up (big to small) output/input. input gear turns 1/2 for output to turn once.
    final double WHEEL_DIAMETER_INCHES = 6.0;
    final double COUNTS_PER_INCH = ( COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415); //encoder counts x gear reduction / circumference of wheel
    final double DRIVE_SPEED = .6;
    
    private ElapsedTime runtime = new ElapsedTime();
    
    @Override
    public void runOpMode() {
        //connects motors to hub & phone- use name in quotes for config
        leftMotor = hardwareMap.get(DcMotor.class, "left_Motor");
        rightMotor = hardwareMap.get(DcMotor.class, "right_Motor");
        intakeServo1 = hardwareMap.get(CRServo.class, "leftVexMotor");
        intakeServo2 = hardwareMap.get(CRServo.class, "rightVexMotor");
        intakePivotServo = hardwareMap.get(Servo.class, "intakePivotServo");

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //going to spin wheels w hand and c position change
        telemetry.addData("leftMotor position:", leftMotor.getCurrentPosition());
        telemetry.addData("rightMotor position:", rightMotor.getCurrentPosition());
        telemtry.update();
        
        waitForStart(); //press play button, actives opMode

        intakePivotServo.setPosition(intakePivotServoPos);

        positionLM= leftMotor.getCurrentPosition();
        positionRM= rightMotor.getCurrentPosition();

        targetLM= (positionLM + (int)(COUNTS_PER_INCH*4));
        targetRM= (positionRM + (int)(COUNTS_PER_INCH*4));

        leftMotor.setTargetPosition(targetLM);
        rightMotor.setTargetPosition(targetRM);

        leftMotor.setPower(DRIVE_SPEED);
        rightMotor.setPower(DRIVE_SPEED-.1);//just bc right motors always go faster

        while(rightMotor.isBusy()){ //once @ position, stop robot //lft motors=slower/have w...stuck in loop?
            telemetry.addData("leftMotor ticks:", leftMotor.getCurrentPosition());
            telemetry.addData("rightMotor ticks:", rightMotor.getCurrentPosition());
            telemetry.update();
            idle();
        }
        
        if(opModeIsActive() && (getRuntime()>18.0 && ((leftMotor.getCurrentPosition() <targetLM) || (rightMotor.getCurrentPosition() <targetRM)))      
        {
            leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
            rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
            telemetry.addData("Turned off encoders"," getRuntime());
            telemetry.update();
            leftMotor.setPower(.5);
            rightMotor.setPower(.5); 
            telemetry.addData("Blindly driving"," getRuntime());
            telemetry.update();                  
            sleep(3500);                       
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        }//end opModeIsActive

    }//end runOpMode
}//end class
