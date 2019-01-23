package core.game;

import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.Event;
import api.game.map.Player;
import core.system.error.GameErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Логирование игровых событий
 */
@Component
public class EventLogger {
  private static final Logger logger = LoggerFactory.getLogger(CoreImpl.class);

  public Event logGameEvent(Event event){
    switch (event.getEventType()) {
      case PLAYER_ADDED: {
          // ("Игрок '%s' присоединился к игре '%s'  (контекст '%s'). Теперь в игре %s игроков из %s");
        logger.info(event.getEventType().getFormattedMessage(event.getSource(Player.class).getTitle(), event.getSourceContext().getGameName()
                , event.getSourceContext().getContextId(), String.valueOf(event.getSourceContext().getLevelMap().getPlayers().size())
                        , String.valueOf(event.getSourceContext().getLevelMap().getMaxPlayerCount())));
        break;
      }
      case PLAYER_CONNECTED: {
          // "Игрок '%s' повторно подключился к игре '%s'  (контекст '%s'). В игре %s игроков из %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSource(Player.class).getTitle(), event.getSourceContext().getGameName()
                , event.getSourceContext().getContextId(), String.valueOf(event.getSourceContext().getLevelMap().getPlayers().size())
                        , String.valueOf(event.getSourceContext().getLevelMap().getMaxPlayerCount())));
        break;
      }
      case PLAYER_REMOVED: {
        // "Игрок '%s' покинул игру '%s'  (контекст '%s'). Теперь в игре %s игроков из %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSource(Player.class).getTitle(), event.getSourceContext().getGameName()
                , event.getSourceContext().getContextId()
                , String.valueOf(event.getSourceContext().getLevelMap().getPlayers().size())
                , String.valueOf(event.getSourceContext().getLevelMap().getMaxPlayerCount())));
        break;
      }
      case WARRIOR_ADDED : {
        // "В игре '%s' (контекст '%s') игроком '%s' добавлен воин '%s'"
        logger.info(event.getEventType().getFormattedMessage(
                event.getSourceContext().getGameName()
                , event.getSourceContext().getContextId()
                , event.getSource(Warrior.class).getOwner().getTitle()
                , event.getSource(Warrior.class).getWarriorBaseClass().getTitle()));

        break;
      }
      case WARRIOR_MOVED: {
        // "В игре '%s' игрок '%s' переместил юнит '%s' на координаты '%s'"
        logger.info(event.getEventType().getFormattedMessage(event.getSourceContext().getGameName()
                , event.getSource(Warrior.class).getOwner().getTitle()
                , event.getSource(Warrior.class).getWarriorBaseClass().getTitle()
                , event.getSource(Warrior.class).getCoords().toString()));
        break;
      }
      case WEAPON_TAKEN: {
        // "В игре '%s' игрок '%s' снарядил юнит '%s' (id '%s') оружием '%s'. %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSourceContext().getGameName()
                , event.getSource(Warrior.class).getOwner().getTitle()
                , event.getSource(Warrior.class).getWarriorBaseClass().getTitle()
                , event.getSource(Warrior.class).getWarriorBaseClass().getId()
                , event.getSource(Weapon.class).getTitle()
                , event.getSource(Result.class).toString()));
        break;
      }
      case WEAPON_TRY_TO_DROP: {
        // "В игре '%s' игрок '%s' попытался убрать у юнита '%s' (id '%s') оружие id '%s'. %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSourceContext().getGameName()
                , event.getSource(Warrior.class).getOwner().getTitle()
                , event.getSource(Warrior.class).getWarriorBaseClass().getTitle()
                , event.getSource(Warrior.class).getWarriorBaseClass().getId()
                , event.getSource(String.class)
                , event.getSource(Result.class).toString()));
        break;
      }
      case WEAPON_DROPED: {
        // "В игре '%s' игрок '%s' убрал у юнита '%s' (id '%s') оружие '%s' (id '%s'). %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSourceContext().getGameName()
                , event.getSource(Warrior.class).getOwner().getTitle()
                , event.getSource(Warrior.class).getWarriorBaseClass().getTitle()
                , event.getSource(Warrior.class).getWarriorBaseClass().getId()
                , event.getSource(Weapon.class).getTitle()
                , event.getSource(Weapon.class).getId()
                , event.getSource(Result.class).toString()));
        break;
      }
      default: {
        logger.error(GameErrors.UNKNOWN_EVENT_FOR_LOGGER.getError(event.getEventType().getFormattedMessage()).getMessage());
      }
    }
    return event;
  }
}
