package views;

import controller.MenusController.GameMenuController;
import models.Fundementals.Result;
import models.enums.commands.GameMenuCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu extends AppMenu {
    private final GameMenuController controller = new GameMenuController();

    @Override
    public void check(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = GameMenuCommands.PLAY.getMather(input)) != null) {
            List<String> players = new ArrayList<>();

            for (int i = 1; i <= 4; i++) {
                if (matcher.group(i) != null)
                    players.add(matcher.group(i).trim());
            }

            Result result = controller.Play(scanner, players);
            if (!result.getMessage().isEmpty()) {
                System.out.println(result.getMessage());
            }
        }
    }
    public void showCurrentTime(){}
    public void showCurrentDate(){}
    public void showCurrentSeason(){}
    public void dateTime(){}
    public void dayOfWeek(){}
    public void cheatAdvancedTime(){}
    public void cheatAdvancedDate(){}
}
