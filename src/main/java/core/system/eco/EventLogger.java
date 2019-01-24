package core.system.eco;

import api.core.Context;
import api.core.Result;
import api.entity.warrior.Warrior;
import api.entity.weapon.Weapon;
import api.game.Event;
import api.game.map.Player;
import core.game.CoreImpl;
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

  public Event logGameEvent(Event event) {
    switch (event.getEventType()) {
      case PLAYER_LOGGED_IN: {
        // "Вход игрока %s. %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSource(Player.class).getTitle()
                , event.getSource(Result.class).toString()));
        break;
      }
      case PLAYER_CONNECTED: {
        // "Игрок '%s' присоединился к игре (контекст '%s'). Теперь в игре %s игроков из %s. %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSource(Player.class).getTitle()
                , event.getSourceContext().getContextId()
                , String.valueOf(event.getSourceContext().getLevelMap().getPlayers().size())
                , String.valueOf(event.getSourceContext().getLevelMap().getMaxPlayerCount())
                , event.getSource(Result.class).toString()));
        break;
      }
      case PLAYER_RECONNECTED: {
        // "Игрок '%s' повторно подключился к игре (контекст '%s'). В игре %s игроков из %s. %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSource(Player.class).getTitle()
                , event.getSourceContext().getContextId()
                , String.valueOf(event.getSourceContext().getLevelMap().getPlayers().size())
                , String.valueOf(event.getSourceContext().getLevelMap().getMaxPlayerCount())
                , event.getSource(Result.class).toString()));
        break;
      }
      case PLAYER_DISCONNECTED: {
        // "Игрок '%s' покинул игру (контекст '%s'). Теперь в игре %s игроков из %s. %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSource(Player.class).getTitle()
                , event.getSourceContext().getContextId()
                , String.valueOf(event.getSourceContext().getLevelMap().getPlayers().size())
                , String.valueOf(event.getSourceContext().getLevelMap().getMaxPlayerCount())
                , event.getSource(Result.class).toString()));
        break;
      }
      case WARRIOR_ADDED: {
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
      case GAME_CONTEXT_CREATED: {
        //   "Создание контекста '%s'. %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSourceContext().getContextId()
                , event.getSource(Player.class).getTitle()
                , event.getSource(Result.class).toString()));
        break;
      }
      case GAME_CONTEXT_CREATE: {
        //  "Создание контекста игры '%s' игроком '%'. %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSource(String[].class)[0]
                , event.getSource(String[].class)[1]
                , event.getSource(Result.class).toString()));
        break;
      }
      case GAME_CONTEXT_REMOVED: {
        //  "Удаление контекста игры '%s' (владелец '%s'). %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSource(Context.class).getContextId()
                , event.getSource(Context.class).getContextOwner().getTitle()
                , event.getSource(Result.class).toString()));
        break;
      }
      case GAME_CONTEXT_LOAD_MAP: {
        // "игра '%s' Контекст %s : загрузка карты '%s' игроком '%s'. тип игры %s.  %s"
        logger.info(event.getEventType().getFormattedMessage(event.getSourceContext().getGameName()
                , event.getSourceContext().getContextId()
                , event.getSource(String.class)
                , event.getSource(Player.class).getTitle()
                , event.getSource(Boolean.class) ? "скрытая" : "открыта для всех"
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
