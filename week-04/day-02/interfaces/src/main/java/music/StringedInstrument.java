package main.java.music;

public abstract class StringedInstrument extends Instrument {
  int numberOfStrings;

  protected abstract String sound();

  @Override
  public void play() {
    System.out.println(
        this.name + " a " + this.numberOfStrings + "-stringed instrument that goes " + sound());
  }
}
