import java.util.Scanner;

public class Duke {
    private static Task[] list = new Task[100];
    private static int taskIndex = 0;
    private static Boolean willExit = false;

    public static void printFormat(String s) {
        System.out.println("____________________________________________________________\n" +
                s + "\n" +
                "____________________________________________________________");
    }

    public static void greet() {
        printFormat(" Hey there! I'm Duke\n" +
                " What can I do for you? uwu");
    }

    public static void bye() {
        willExit = true;
        printFormat(" Aw, are you leaving now?\n" +
                " Hope to see you again soon!");
    }

    public static void list() {
        if (taskIndex == 0) {
            printFormat("You haven't added any tasks to your list yet!");
            return;
        }

        String listAsString = "";
        for (int i = 0; i < taskIndex; i++) {
            Task curr = list[i];
            listAsString = listAsString.concat(String.format(" %d. %s\n", i + 1, curr.toString()));
        }
        printFormat("Here are the tasks in your list:\n" + listAsString);
    }

    public static void markStatus(Boolean shouldMark, String line) {
        Task curr;
        try {
            int taskNum = Integer.parseInt(line.split(" ", 0)[1]);
            curr = list[taskNum - 1];
        } catch (Exception exception) {
            printFormat("Please mark / unmark with a number that's in the list :')");
            return;
        }

        if (shouldMark) {
            curr.setDone(true);
            printFormat("Nice! I've marked this task as done:\n  " + curr.toString());
        } else {
            curr.setDone(false);
            printFormat("OK, I've marked this task as not done yet:\n  " + curr.toString());
        }
    }

    private static Task parseDeadline(String description) {
        String[] deadlineBreakdown = description.split("/by", 2);
        description = deadlineBreakdown[0];
        String by = deadlineBreakdown[1];
        Task t = new Deadline(description, by);
        return t;
    }
    private static Task parseEvent(String description) {
        String[] eventBreakdown = description.split(" /at ", 2);
        description = eventBreakdown[0];
        String at = eventBreakdown[1];
        Task t = new Event(description, at);
        return t;
    }

    public static void invalidTask() {
        printFormat("I don't understand what you want to do.\n" +
                "Maybe you could try the following commands:\n" +
                "  - list: list out existing tasks\n" +
                "  - etc.");
    }

    public static void addTask(String line) {
        Task t;
        try {
            String[] commands = line.split(" ", 2);
            String type = commands[0];
            String description = commands[1];

            if (type.equals("todo")) {
                t = new Todo(description);
            } else if (type.equals("deadline")) {
                t = parseDeadline(description);
            } else if (type.equals("event")) {
                t = parseEvent(description);
            } else {
                throw new java.lang.RuntimeException("Not a valid task type");
            }
            list[taskIndex] = t;
            taskIndex++;
            printFormat("Got it. I've added this task:\n  " + t.toString() +
                    String.format("\nNow you have %d tasks in the list.", taskIndex));
        } catch (Exception e){
            invalidTask();
        }
    }

    public static void parseCommands(String line) {
        if (line.equals("bye")) {
            bye();
        } else if (line.equals("list")) {
            list();
        } else if (line.startsWith("mark")) {
            markStatus(true, line);
        } else if (line.startsWith("unmark")) {
            markStatus(false, line);
        } else {
            addTask(line);
        }
    }

    public static void main(String[] args) {
        greet();
        String line;
        Scanner in = new Scanner(System.in);

        while (!willExit) {
            line = in.nextLine();
            parseCommands(line);
        }
    }
}
