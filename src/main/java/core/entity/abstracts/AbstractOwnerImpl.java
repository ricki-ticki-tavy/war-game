package core.entity.abstracts;

import api.core.Context;
import api.core.Owner;
import api.enums.OwnerTypeEnum;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Базовый класс всех наследников Owner
 */
public abstract class AbstractOwnerImpl<O extends Owner>   implements Owner{
  protected String id;
  protected String title;
  protected String description;
  protected O owner;
  private Context context;
  protected OwnerTypeEnum thisOwnerType;

  protected AbstractOwnerImpl(O owner, OwnerTypeEnum thisOwnerType, String idPrefix, String title, String description){
    this.owner = owner;
    this.context = owner == null ? null : owner.getContext();
    this.id = (StringUtils.isEmpty(idPrefix) ? "art_" :  idPrefix) + "_" + UUID.randomUUID().toString();
    this.title = title;
    this.description = description;
    this.thisOwnerType = thisOwnerType == null ? OwnerTypeEnum.SYSTEM : thisOwnerType;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Context getContext() {
    if (context == null && owner != null){
      context = owner.getContext();
    }
    return context;
  }

  @Override
  public OwnerTypeEnum getThisOwnerType() {
    return thisOwnerType;
  }

  @Override
  public O getOwner() {
    return owner;
  }

  protected AbstractOwnerImpl setContext(Context context){
    this.context = context;
    return this;
  }
}
