package project3.gameMech;

import project3.gameRender;

import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.*;

class GameMode extends JPanel implements MouseMotionListener, KeyListener {
  private int cx = 3, cy = 3, ballSpeed = 3; // to make it harder, increase all THREE variables
  private int userPaddleSpeed = 2;
  private int pcPaddleSpeed = 1;
  private int refreshRate = gameRender.DELAY; // want to change this? change main's delay
  private Color pcPaddleColor = Color.RED, userPaddleColor = Color.BLUE, ballColor = Color.YELLOW;
  protected boolean pcAccidentalMiss;
  private int oscillationFrequency = 10;
  private int userPaddleHeight = 75;
  private int pcPaddleHeight = 75;
  private int intUserLoc = 200;
  private int intPcLoc = 200;
  private int swingState = 75;
  private int percentChance = 3;

  private boolean dynamicBallSpeed = true;
  private boolean betterAi = true;
  private boolean oscillation = true;
  private boolean acMissMode = true;
  private boolean multiplayer = false;
  private boolean disableMouse = false;
  private boolean cryBabyChance = false;

  public int getCx() {
    return cx;
  }

  public int getCy() {
    return cy;
  }

  public int getBallSpeed() {
    return ballSpeed;
  }

  public int getUserPaddleSpeed() {
    return userPaddleSpeed;
  }

  public int getPcPaddleSpeed() {
    return pcPaddleSpeed;
  }

  public int getRefreshRate() {
    return refreshRate;
  }

  public Color getPcPaddleColor() {
    return pcPaddleColor;
  }

  public Color getUserPaddleColor() {
    return userPaddleColor;
  }

  public Color getBallColor() {
    return ballColor;
  }

  public int getOscillationFrequency() {
    return oscillationFrequency;
  }

  public int getUserPaddleHeight() {
    return userPaddleHeight;
  }

  public int getPcPaddleHeight() {
    return pcPaddleHeight;
  }

  public boolean getDynamicBallSpeed() {
    return dynamicBallSpeed;
  }

  public boolean getBetterAi() {
    return betterAi;
  }

  public boolean getOscillation() {
    return oscillation;
  }

  public boolean getAcMissMode() {
    return acMissMode;
  }

  public boolean getMultiplayer() {
    return multiplayer;
  }

  public boolean getDisableMouse() {
    return disableMouse;
  }

  public int getIntUserLoc() {
    return intUserLoc;
  }

  public int getIntPcLoc() {
    return intPcLoc;
  }

  public boolean getCryBabyChance() {
    return cryBabyChance;
  }

  public int getSwingState() {
    return swingState;
  }

  public int getPercentChance() {
    return percentChance;
  }

  // Compact setter methods
  public void setCx(int cx) {
    this.cx = cx;
  }

  public void setCy(int cy) {
    this.cy = cy;
  }

  public void setBallSpeed(int ballSpeed) {
    this.ballSpeed = ballSpeed;
  }

  public void setUserPaddleSpeed(int userPaddleSpeed) {
    this.userPaddleSpeed = userPaddleSpeed;
  }

  public void setPcPaddleSpeed(int pcPaddleSpeed) {
    this.pcPaddleSpeed = pcPaddleSpeed;
  }

  public void setRefreshRate(int refreshRate) {
    this.refreshRate = refreshRate;
  }

  public void setPcPaddleColor(Color pcPaddleColor) {
    this.pcPaddleColor = pcPaddleColor;
  }

  public void setUserPaddleColor(Color userPaddleColor) {
    this.userPaddleColor = userPaddleColor;
  }

  public void setBallColor(Color ballColor) {
    this.ballColor = ballColor;
  }

  public void setOscillationFrequency(int oscillationFrequency) {
    this.oscillationFrequency = oscillationFrequency;
  }

  public void setUserPaddleColor(int userPaddleHeight) {
    this.userPaddleHeight = userPaddleHeight;
  }

  public void setUserPaddleHeight(int userPaddleHeight) {
    this.userPaddleHeight = userPaddleHeight;
  }

  public void setPcPaddleHeight(int pcPaddleHeight) {
    this.pcPaddleHeight = pcPaddleHeight;
  }

  public void setDynamicBallSpeed(boolean dynamicBallSpeed) {
    this.dynamicBallSpeed = dynamicBallSpeed;
  }

  public void setBetterAi(boolean betterAi) {
    this.betterAi = betterAi;
  }

  public void setOscillation(boolean oscillation) {
    this.oscillation = oscillation;
  }

  public void setAcMissMode(boolean acMissMode) {
    this.acMissMode = acMissMode;
  }

  public void setMultiplayer(boolean multiplayer) {
    this.multiplayer = multiplayer;
  }

  public void setDisableMouse(boolean disableMouse) {
    this.disableMouse = disableMouse;
  }

  public void setIntUserLoc(int intUserLoc) {
    this.intUserLoc = intUserLoc;
  }

  public void setIntPcLoc(int intPcLoc) {
    this.intPcLoc = intPcLoc;
  }

  public void setCryBabyChance(boolean cryBabyChance) {
    this.cryBabyChance = cryBabyChance;
  }

  public void setSwingState(int swingState) {
    this.swingState = swingState;
  }

  public void setPercentChance(int percentChance) {
    this.percentChance = percentChance;
  }

  public void setTotalBallspeed(int sdp) {
    setCx(sdp);
    setCy(sdp);
    setBallSpeed(sdp);
  }

  public GameMode() {
    gameModeSetter();
  }

  public void gameModeSetter() {
    /*
    *  setDynamicBallSpeed(true);
    *  setBetterAi(true); //multiplayer disables this
    *  setOscillation(true); //multiplayer disables this
    *  setAcMissMode(true); //multiplayer disables this
    *  //setMultiplayer(true);
    *  //setPcPaddleHeight(3000);
    *  setDisableMouse(true); //multiplayer disables this
    * 
    *  setCx(6);
    *  setCy(6);
    *  setBallSpeed(6);
    * 
    *  setPcPaddleSpeed(6);
    *  setUserPaddleSpeed(4);
    *  setOscillation(false); //multiplayer disables this
    *  setUserPaddleHeight(3000);
    *  setAcMissMode(false);
    *  setIntUserLoc(0);
    */

    // userChinaWall();
    // cryBabyMode();
    // casual();
    // intimidating();
    // meVsSkynet();
    // deathwish();
    // jail();
    multiCasual();
    // pingPongDiplomacy();
    // multiIntimidating();
    // DPRKAthelete();
  }

  public void cryBaby() {
    setDynamicBallSpeed(false);
    setBetterAi(false);
    setAcMissMode(true);
    // setOscillation(true); //if better AI is turned off, oscillation will not work
    // anyway, so this line has no effect
    setCryBabyChance(true);

  }

  public void casual() {
    // everything is set to default
  }

  public void intimidating() {
    setTotalBallspeed(3);
    setUserPaddleSpeed(3);
    setPcPaddleSpeed(3);
    setOscillationFrequency(20);
    setPercentChance(6);
  }

  public void meVsSkynet() {
    setTotalBallspeed(5);
    setDynamicBallSpeed(true);
    setBetterAi(true);
    setAcMissMode(false);
    setOscillation(false);
    setPcPaddleSpeed(6);
    setUserPaddleSpeed(6);

    // userChinaWall();
  }

  public void deathwish() {
    setTotalBallspeed(15);
    setDynamicBallSpeed(true);
    setBetterAi(true);
    setAcMissMode(false);
    setOscillation(false);
    setPcPaddleSpeed(15);
    setUserPaddleSpeed(8);
  }

  public void jail() {
    setTotalBallspeed(5);
    setDynamicBallSpeed(false);
    setBetterAi(false);
    setAcMissMode(false);
    setOscillation(false);
    setPcPaddleSpeed(0);
    setUserPaddleSpeed(3);
    setIntPcLoc(0);
    setPcPaddleHeight(1000);
  }

  public void multiCasual() {
    setMultiplayer(true);
  }

  public void multiIntimidating() {
    setMultiplayer(true);
    setTotalBallspeed(5);
    setUserPaddleSpeed(4);
    setPcPaddleSpeed(4);
  }

  public void pingPongDiplomacy() {
    setMultiplayer(true);
    setTotalBallspeed(5);
    setUserPaddleSpeed(6);
    setPcPaddleSpeed(6);
  }

  public void DPRKAthelete() {
    setMultiplayer(true);
    setTotalBallspeed(15);
    setUserPaddleSpeed(8);
    setPcPaddleSpeed(8);
  }

  public void userChinaWall() {
    setIntUserLoc(0);
    setUserPaddleHeight(1000);
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    System.out.println("FUCK YOU NIGGER");
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {

  }
}