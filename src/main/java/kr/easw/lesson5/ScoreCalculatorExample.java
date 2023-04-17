package kr.easw.lesson5;

import java.util.Scanner;


/**
 * 해당 문제는 사용자의 입력으로 점수를 받고, 모든 점수를 받았다면 백분율과 최고 점수를 반환하는 코드를 작성해야 합니다.
 */
public class ScoreCalculatorExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("입력할 점수 개수를 입력하십시오 : ");
        ScoreData[] array = generateArray(scanner.nextInt());
        fillArray(scanner, array);
        for (ScoreData score : array) {
            System.out.printf("과목: %s (%d점, %f%%)\n", score.name, score.score, getPercentage(array, score));
        }
        ScoreData highestScore = getHighestScore(array);
        System.out.printf("최고 점수: %s (%d점, %f%%)", highestScore.name, highestScore.score, getPercentage(array, highestScore));
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 주어진 값만큼의 크기를 갖는 배열을 반환해야 합니다.
     */
    public static ScoreData[] generateArray(int size) {
        // 주어진 크기만큼 배열을 생성하여 반환합니다.
        return new ScoreData[size];
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 주어진 {@link Scanner} 변수를 사용해 배열을 채워야 합니다.
     * <p>
     * 입력되는 값은 과목 이름(String), 과목 점수(Int)입니다.
     */
    public static void fillArray(Scanner scanner, ScoreData[] array) {
        // 배열의 크기만큼 반복합니다.
        for (int i = 0; i < array.length; i++) {
            System.out.print("과목명을 입력하십시오 : ");
            // Scanner을 통해 점수 이름을 받습니다.
            String scoreName = scanner.next();
            System.out.print("점수를 입력하십시오 : ");
            // Scanner을 통해 점수를 받습니다.
            int scoreValue = scanner.nextInt();
            // 배열에 ScoreData 인스턴스를 설정합니다.
            array[i] = new ScoreData(scoreName, scoreValue);
        }
    }

    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 입력된 data 파라미터가 현재 배열의 점수를 합산한 값의 몇퍼센트인지 백분율로 반환해야 합니다.
     * <p>
     * 예를 들어, 입력된 과목의 점수가 10이고, 전체 전수가 100이면 10을 반환해야 합니다.
     */
    public static double getPercentage(ScoreData[] array, ScoreData data) {
        // 현재 전체 점수를 합한 값을 보관할 변수를 선언합니다.
        double total = 0;
        // 배열의 안에 있는 점수를 for-each로 반복하여 가져옵니다.
        for (ScoreData scoreData : array) {
            // total에 점수를 더합니다.
            total += scoreData.score;
        }
        // 전체 값으로 현재 값을 나누면 현재 백분율을 100으로 나눈 값이 나옵니다.
        // 예를 들어, 2를 10으로 나누면 0.2가 됩니다.
        // 이러한 연산은 double과 float에서만 가능하기에, double로 값 연산을 진행합니다.
        // 추가로, 100을 곱해 현재 백분율로 반환합니다.
        return ((double) data.score / total) * 100.0;
    }


    /**
     * 해당 메서드는 다음과 같은 역할을 가져야 합니다 :
     * 가장 높은 과목의 인스턴스를 반환해야 합니다.
     */
    public static ScoreData getHighestScore(ScoreData[] array) {
        // 최고점을 보관할 변수를 선언합니다.
        ScoreData highest = null;
        // 배열의 안에 있는 점수를 for-each로 반복하여 가져옵니다.
        for (ScoreData scoreData : array) {
            // 만약 최고점이 설정되지 않았다면,
            if (highest == null) {
                // 비교하지 않고 현재 점수를 최고점으로 설정합니다.
                highest = scoreData;
                continue;
            }
            // 만약 최고점 변수가 현재 점수보다 점수가 낮다면,
            if (highest.score < scoreData.score) {
                // 최고점 변수를 현재 점수로 반환합니다.
                highest = scoreData;
            }
        }
        // 현재 확인된 최고점 인스턴스를 반환합니다.
        return highest;
    }

    private static class ScoreData {
        private final String name;

        private final int score;

        public ScoreData(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }
}
