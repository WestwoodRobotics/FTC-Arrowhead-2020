package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "TeleOp2", group = "Iterative Opmode")

public class ArrowheadTeleOp extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    //initialize motors
    private DcMotor frontLeft, frontRight, backLeft, backRight, intakeLeft, intakeRight, shooter, arm;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        //put all motors on the hardware map
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        intakeLeft = hardwareMap.get(DcMotor.class, "intakeLeft");
        intakeRight = hardwareMap.get(DcMotor.class, "intakeRight");
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        arm = hardwareMap.get(DcMotor.class, "arm");


        //set turn directions of all motors
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        intakeLeft.setDirection(DcMotor.Direction.FORWARD);
        intakeRight.setDirection(DcMotor.Direction.FORWARD);
        shooter.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotor.Direction.FORWARD);

        //tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {

        //for simplicity and easier readability
        double leftStickX = gamepad1.left_stick_x;
        double leftStickY = -gamepad1.left_stick_y;
        double turnNum = 0;

        if (gamepad1.left_bumper && !gamepad1.right_bumper) {
            turnNum = -1;
        } else if (!gamepad1.left_bumper && gamepad1.right_bumper) {
            turnNum = 1;
        } else {
            turnNum = 0;
        }

        //sets power of arm motor to left stick y of controller 2
        arm.setPower(gamepad2.left_stick_y);

        //left trigger reverses shooter
        //right trigger shoots
        if (gamepad2.left_trigger != 0) {
            shooter.setPower(-1);
        } else if (gamepad2.right_trigger != 0) {
            shooter.setPower(1);
        } else {
            shooter.setPower(0);
        }

        /*
        intake controls:
        a -- right forward
        b -- right backward
        y -- left forward
        x -- right forward
        */
        if (gamepad2.a) {
            intakeRight.setPower(1);
        } else if (gamepad2.b) {
            intakeRight.setPower(-1);
        } else {
            intakeRight.setPower(0);
        }

        if (gamepad2.y) {
            intakeLeft.setPower(1);
        } else if (gamepad2.x) {
            intakeLeft.setPower(-1);
        } else {
            intakeLeft.setPower(0);
        }

        //set powers for all motors accounting for weird orientation
        double frontLeftPower = -leftStickY + leftStickX + turnNum ;
        double frontRightPower = leftStickY + leftStickX - turnNum;
        double backLeftPower = leftStickY + leftStickX + turnNum;
        double backRightPower = -leftStickY + leftStickX - turnNum;
        double maxPower = Math.max(Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)), Math.abs(backLeftPower)), Math.abs(backRightPower));

        //scales down power variables to fit within range [-1, 1]
        if (maxPower > 1) {
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;
            backLeftPower /= maxPower;
            backRightPower /= maxPower;
        }

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);

        //show the elapsed game time and wheel power
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "frontLeft (%.2f), frontRight (%.2f), backLeft (%.2f), backRight (%.2f)", frontLeftPower, frontRightPower, backLeftPower, backRightPower);
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}