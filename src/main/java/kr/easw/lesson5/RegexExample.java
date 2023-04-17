package kr.easw.lesson5;

import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 해당 문제는 문자열을 받아 이 문자열이 시간인지 확인하고, 시간이라면 초로 환산하는 코드를 구현해야 합니다.
 * 해당 문제를 푸는 이상적인 정답은 정규식(Regex)를 사용하는것이나, 너무 어려울 경우 문자열 연산만을 사용해도 됩니다.
 * <p>
 * 모든 정상적인 입력은 다음 포맷을 따라야 합니다 :
 * <p>
 * "x시간 y분 z초"
 * <p>
 * 다시 말해, 다음과 같은 시간은 정상 문자열입니다 :
 * <p>
 * "1시간 15분 20초"
 */
public class RegexExample {
    public static final String EXAMPLE_TIME = "1시간 0분 15초";
    public static final int EXAMPLE_RESULT = 3615;
    private static final DecimalFormat format = new DecimalFormat("#,###");

    // 재사용할 패턴을 선언합니다.
    // 패턴에서, \\d+은 이 부분에 숫자가 들어올것이라는것을 의미합니다.
    // 일반적으로는 시간과 분, 초에 엄격한 패턴을 적용하지만 해당 파일은 예제임으로 단순하게 사용합니다.
    private static final Pattern pattern = Pattern.compile("(\\d+)시간 (\\d+)분 (\\d+)초");

    public static void main(String[] args) {
        if (!isValidTimeFormat(EXAMPLE_TIME) || formatTime(EXAMPLE_TIME.split(" ")) != EXAMPLE_RESULT) {
            System.out.println("오답입니다.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("초로 변환할 시간을 입력하십시오 : ");
        String next = scanner.nextLine();
        if (!isValidTimeFormat(next)) {
            System.out.println("잘못된 포맷입니다.");
            return;
        }
        System.out.println(next + "는 " + format.format(formatTime(next.split(" "))) + "초입니다.");
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 입력된 문자열이 조건에 일치하는 문자열이라면 true, 아니라면 false를 반환해야 합니다.
     * <p>
     * 허용되는 문자열 조건은 다음과 같습니다 :
     * <p>
     * "x시간 y분 z초"
     * <p>
     * 다시 말해, 다음과 같은 시간은 정상 문자열입니다 :
     * <p>
     * "1시간 15분 20초"
     */
    private static boolean isValidTimeFormat(String str) {
        // 미리 선언해둔 패턴의 Matcher 인스턴스를 생성합니다.
        Matcher matcher = pattern.matcher(str);
        // 패턴과 일치하는지 확인합니다.
        // matches는 주어진 패턴과 완전히 일치하는지의 여부를 반환합니다.
        return matcher.matches();
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * <p>
     * 입력된 문자열을 초로 환산해야 합니다.
     * <p>
     * 해당 메서드를 통해 입력되는 모든 문자열은 이미 조건에 일치하는 조건이라고 가정되며, 항상 다음의 포맷을 따릅니다 :
     * <p>
     * "x시간 y분 z초"
     */
    private static int formatTime(String[] string) {
        // 이미 모든 데이터가 포맷에 맞다고 가정하기 때문에, 단순히 문자열 연산으로만 진행합니다.
        // 시간 포맷은 n시간을 따르기 때문에, 마지막 문자열에서 2개의 문자를 자르고, int로 변환합니다.
        int hour = Integer.parseInt(string[0].substring(0, string[0].length() - 2));
        // 분 포맷은 n분을 따르기 때문에, 마지막 문자열에서 1개의 문자를 자르고, int로 변환합니다.
        int minute = Integer.parseInt(string[1].substring(0, string[1].length() - 1));
        // 초 포맷은 n초를 따르기 때문에, 마지막 문자열에서 1개의 문자를 자르고, int로 변환합니다.
        int second = Integer.parseInt(string[2].substring(0, string[2].length() - 1));
        // 이제, 변환한 모든 문자열의 연산을 진행합니다 :
        return
                // 시간에 3600 (60분 * 60초)를 곱하고,
                hour * 3600 +
                // 분에 60 (60초)를 곱하고,
                minute * 60 +
                // 마지막으로 초 단위까지 모두 더해 반환합니다.
                second;
    }
}
