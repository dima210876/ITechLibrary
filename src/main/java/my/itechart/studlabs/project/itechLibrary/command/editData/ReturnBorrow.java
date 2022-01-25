package my.itechart.studlabs.project.itechLibrary.command.editData;

import my.itechart.studlabs.project.itechLibrary.command.Command;
import my.itechart.studlabs.project.itechLibrary.command.RequestContext;
import my.itechart.studlabs.project.itechLibrary.command.ResponseContext;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ReturnBorrow implements Command
{
    INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request)
    {
        return null;
    }
//    long daysAfter = Duration.between(borrow.getReturnDate(), LocalDate.now()).toDays();
//    Double resultCost = damagePenalty;
//    resultCost += daysAfter > 0 ? borrow.getCost() * (1 + 0.01 * daysAfter) : borrow.getCost();

//    private List<String> parseStringIntoStringList(String set) {
//        return Arrays.stream(set.split(", ")).distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
//    }

}
