package org.spbstu.file_host.util.naming;

import org.spbstu.file_host.exception.server.CannotFindFreeNumberForFileNameException;
import org.spbstu.file_host.util.FileSystemObjectUtil;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("fileNamingStrategy")
public class IndexNumberNamingStrategy implements NamingStrategy {
    private final String counterPatternString = "_\\d+$";
    private final Pattern counterPattern = Pattern.compile(counterPatternString);

    @Override
    public String apply(Stream<String> notAvailableNames, String baseName) {
        Set<String> names = notAvailableNames.collect(Collectors.toSet());
        return names.contains(baseName) ?
                FileSystemObjectUtil.removeExtension(baseName) + "_"
                        + getFreeNumber(getBusyNumbersFromNamesLikePatternAsStream(names, baseName))
                        + FileSystemObjectUtil.getExtension(baseName)
                : baseName;
    }

    private Stream<Integer> getBusyNumbersFromNamesLikePatternAsStream(Set<String> names, String fileName) {
        String extension = FileSystemObjectUtil.getExtension(fileName);
        Pattern rawFileNamePattern = Pattern.compile("^" + FileSystemObjectUtil.removeExtension(fileName) + counterPatternString);
        return names
                .stream()
                .filter(s -> s.endsWith(extension))
                .map(FileSystemObjectUtil::removeExtension)
                .map(counterPattern::matcher)
                .filter(Matcher::find)
                .map(Matcher::group)
                .map(s -> s.replace("_", ""))
                .map(Integer::parseInt);
    }

    private int getFreeNumber(Stream<Integer> streamOfBusyNumbers) {
        int[] orderedListOfUniqueBusyNumbers = streamOfBusyNumbers.distinct().sorted()
                .mapToInt(value -> value)
                .toArray();
        if (orderedListOfUniqueBusyNumbers.length == 0 ||
                orderedListOfUniqueBusyNumbers[orderedListOfUniqueBusyNumbers.length - 1] ==
                        orderedListOfUniqueBusyNumbers.length - 1)
            return orderedListOfUniqueBusyNumbers.length;
        for (int i = 0; i < orderedListOfUniqueBusyNumbers.length; i++) {
            if (orderedListOfUniqueBusyNumbers[i] != i)
                return i;
        }
        throw new CannotFindFreeNumberForFileNameException();
    }
}
