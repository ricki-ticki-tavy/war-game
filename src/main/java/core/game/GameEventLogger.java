package core.game;

import api.entity.warrior.Warrior;
import api.game.GameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static api.enums.EventParamNames.WARRIOR_PARAM;

/**
 * Логирование игровых событий
 */
@Component
public class GameEventLogger {
  private static final Logger logger = LoggerFactory.getLogger(CoreImpl.class);

  public GameEvent logGameEvent(GameEvent event){
    switch (event.getEventType()) {
      case WARRIOR_ADDED : {
                logger.info(event.getEventType().getFormattedMessage(event.getSourceContext().getGameName(), event.getSourceContext().getContextId()
                , event.getSource().getTitle(), ((Warrior)event.getParams().get(WARRIOR_PARAM)).getWarriorBaseClass().getTitle() ));

        break;
      }
      case PLAYER_REMOVED: {
        logger.info(event.getEventType().getFormattedMessage(event.getSource().getTitle(), event.getSourceContext().getGameName()
                , event.getSourceContext().getContextId()));
        break;
      }
      case WARRIOR_MOVED: {
        logger.info(event.getEventType().getFormattedMessage(event.getSourceContext().getGameName()
                , ((Warrior)event.getSource()).getOwner().getTitle()
                , ((Warrior)event.getSource()).getWarriorBaseClass().getTitle()
                , ((Warrior)event.getSource()).getCoords().toString()));
        break;
      }
    }
    return event;
  }
}
