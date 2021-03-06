package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

//------------------------------------------------------------------------------
//
// PushBotHardware
//

/**
 * Provides a single hardware access point between custom op-modes and the
 * OpMode class for the Push Bot.
 *
 * This class prevents the custom op-mode from throwing an exception at runtime.
 * If any hardware fails to map, a warning will be shown via telemetry data,
 * calls to methods will fail, but will not cause the application to crash.
 *
 * @author SSI Robotics
 * @version 2015-08-13-20-04
 */
public class LiDwarvenBotHardware extends LinearOpMode
{
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor armAngle;
    DcMotor armReach;

    Servo rightClimberServo;
    Servo leftClimberServo;

    Servo armServo;

    public static final String TAG = "DwarvenDebug";

    public LiDwarvenBotHardware()
    {
        //Constructor
    }

    public void runOpMode() throws InterruptedException {

    }

    public void defineMotors()
    {
        try
        {
            leftDrive = hardwareMap.dcMotor.get("leftDrive");
        }
        catch (Exception p_exeception){}

        try
        {
            rightDrive = hardwareMap.dcMotor.get("rightDrive");
        }
        catch (Exception p_exeception){}

        try
        {
            rightDrive.setDirection(DcMotor.Direction.REVERSE);
        }
        catch (Exception p_exeception){}

        try
        {
            //rightClimberServo = hardwareMap.servo.get("rightClimberServo");
        }
        catch (Exception p_exeception){}

        try
        {
            //leftClimberServo = hardwareMap.servo.get("leftClimberServo");
        }
        catch (Exception p_exeception){}

        try
        {
            armAngle = hardwareMap.dcMotor.get("armAngle");
        }
        catch (Exception p_exception){}

        try
        {
            armReach = hardwareMap.dcMotor.get("armReach");
        }
        catch (Exception p_exception){}
    }

    

    void enableEncoders()
    {
        leftDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    void moveToPosition(int distance)
    {
        if(distance > 0)
            setMotors(1.0, 1.0);
        else
            setMotors(-1.0, -1.0);


        while(Math.abs(getRightEncoder()) < Math.abs(distance * 85))//Ticks per inch
        {
            //wait
        }
        setMotors(0.0, 0.0);
    }

    void rotateToPosition(int degrees)
    {
        if(degrees > 0)
        {
            setMotors(1.0, -1.0);
            while(Math.abs(getRightEncoder()) < Math.abs(degrees * 21))
            {
                //wait
            }
            setMotors(0.0, 0.0);
            return;
        }
        else
        {
            setMotors(-1.0, 1.0);
            while(Math.abs(getLeftEncoder()) < Math.abs(degrees * 21));
            {
                //wait
            }
            setMotors(0.0, 0.0);
            return;
        }
    }

    void setMotors(double powerL, double powerR)
    {
        Log.d(TAG, "Setting motors to: " + powerL + ", " + powerR);
        if(leftDrive != null)
            leftDrive.setPower(powerL);
        if(rightDrive != null)
            rightDrive.setPower(powerR);
    }

    void timeDrive(int distance)
    {
        if(distance < 0)
        {
            setMotors(-1.0, -1.0);
        }
        else
        {
            setMotors(1.0, 1.0);
        }

        try
        {
            wait(distance * 500); //Constant is a modifier for making movement accurate.  Needs calibration.
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        setMotors(0.0, 0.0);
    }

    void timeTurn(int degrees, boolean isLeft)
    {
        if(isLeft)
        {
            setMotors(-1.0, 1.0);
        }
        else
        {
            setMotors(1.0, -1.0);
        }

        try
        {
            wait(degrees * 500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        setMotors(0.0, 0.0);
    }




    void resetEncoders() throws InterruptedException
    {
        if(rightDrive.getMode() != DcMotorController.RunMode.RESET_ENCODERS)
        {
            rightDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
            leftDrive.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        while(getRightEncoder() != 0)
        {
            waitOneFullHardwareCycle();
        }
        rightDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftDrive.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        return;
    }

    int getRightEncoder()
    {
        Log.d(TAG, "Right Encoder " + Integer.toString(rightDrive.getCurrentPosition()));
        return rightDrive.getCurrentPosition();
    }

    int getLeftEncoder()
    {
        Log.d(TAG, "Left Encoder " + Integer.toString(leftDrive.getCurrentPosition()));
        return leftDrive.getCurrentPosition();
    }

    boolean haveEncodersReached(double count)
    {
        if(hasLeftEncoderReached(count))
        {
            return true;
        }
        if(hasLeftEncoderReached(count))
        {
            return true;
        }
        return false;
    }

    boolean hasRightEncoderReached(double count)
    {
        if(Math.abs(getRightEncoder()) >= count)
        {
            return true;
        }
        return false;
    }

    boolean hasLeftEncoderReached(double count)
    {
        if(Math.abs(getLeftEncoder()) >= count)
        {
            return true;
        }
        return false;
    }

    boolean haveEncodersReset()
    {
        if(getRightEncoder() == 0 && getLeftEncoder() == 0)
        {
            return true;
        }
        return false;
    }

    int debugCount = 0;

    void setServos(double rightArm, double leftArm)
    {
        if(rightClimberServo != null && rightArm != -1)
            rightClimberServo.setPosition(rightArm);
        if(leftClimberServo != null  && leftArm != -1)
            leftClimberServo.setPosition(leftArm);
    }

    void print(String message, int slot)
    {
//        switch(slot)
//        {
//            case 1:
//                telemetry.addData("Print 2", message);
//                break;
//            case 2:
//                telemetry.addData("Print 3", message);
//                break;
//            default:
//                telemetry.addData("Print 1", message);
//                break;
//        }
        telemetry.addData("Debug" + String.format("%03d", debugCount), message);
        debugCount++;
    }

    /*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
    double scaleInput(double dVal)
    {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}