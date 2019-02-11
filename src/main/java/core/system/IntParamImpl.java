package core.system;

import api.core.IntParam;

public class IntParamImpl implements IntParam{
  private int value;
  private String title;

  @Override
  public String getId() {
    return title;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return title;
  }

  @Override
  public int getIntValue() {
    return value;
  }

  public IntParamImpl(String title, int value){
    this.title = title;
    this.value = value;
  }

  public IntParamImpl(int value){
    this.title = "";
    this.value = value;
  }


}
