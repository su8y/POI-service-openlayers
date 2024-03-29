package com.example.core.route.model;

/**
 * 경로 안내 방향
 */
public enum GuideCode {
    STRAIGHT_DIRECTION(1),
    TURN_LEFT(2),
    TURN_RIGHT(3),
    LEFT_DIRECTION(4),
    RIGHT_DIRECTION(5),
    U_TURN(6),
    UNSIGNALIZED_LEFT_TURN(8),
    LEFT_AT_8_O_CLOCK(11),
    LEFT_AT_9_O_CLOCK(12),
    LEFT_AT_11_O_CLOCK(13),
    RIGHT_AT_1_O_CLOCK(14),
    RIGHT_AT_3_O_CLOCK(15),
    RIGHT_AT_4_O_CLOCK(16),
    STRAIGHT_AT_ROTOR(21),
    U_TURN_AT_ROTOR(22),
    LEFT_AT_7_O_CLOCK(23),
    LEFT_AT_8_O_CLOCK_AT_ROTOR(24),
    LEFT_AT_9_O_CLOCK_AT_ROTOR(25),
    LEFT_AT_10_O_CLOCK_AT_ROTOR(26),
    LEFT_AT_11_O_CLOCK_AT_ROTOR(27),
    CLOCK_12_AT_ROTOR(28),
    RIGHT_AT_1_O_CLOCK_AT_ROTOR(29),
    RIGHT_AT_2_O_CLOCK_AT_ROTOR(30),
    RIGHT_AT_3_O_CLOCK_AT_ROTOR(31),
    RIGHT_AT_4_O_CLOCK_AT_ROTOR(32),
    RIGHT_AT_5_O_CLOCK_AT_ROTOR(33),
    CLOCK_6_AT_ROTOR(34),
    ENTER_LEFT_ROADWAY(41),
    ENTER_RIGHT_ROADWAY(42),
    ENTER_REST_AREA(47),
    ENTER_FERRY_ROUTE(48),
    EXIT_FERRY_ROUTE(49),
    ENTER_HIGHWAY_AHEAD(50),
    EXIT_HIGHWAY_AHEAD(51),
    ENTER_CITY_HIGHWAY_AHEAD(52),
    EXIT_CITY_HIGHWAY_AHEAD(53),
    ENTER_BRANCH_ROAD_AHEAD(54),
    ENTER_OVERPASS(55),
    ENTER_UNDERPASS(56),
    ENTER_HIGHWAY_ON_THE_LEFT(57),
    EXIT_HIGHWAY_ON_THE_LEFT(58),
    ENTER_CITY_HIGHWAY_ON_THE_LEFT(59),
    EXIT_CITY_HIGHWAY_ON_THE_LEFT(60),
    ENTER_OVERPASS_ON_THE_LEFT(62),
    SIDE_ROAD_AT_THE_OVERPASS_ON_THE_LEFT(63),
    ENTER_UNDERPASS_ON_THE_LEFT(64),
    SIDE_ROAD_AT_THE_UNDERPASS_ON_THE_LEFT(65),
    ENTER_EXPRESSWAY_AHEAD(75),
    ENTER_EXPRESSWAY_ON_THE_LEFT(76),
    ENTER_EXPRESSWAY_ON_THE_RIGHT(77),
    EXIT_EXPRESSWAY_AHEAD(78),
    EXIT_EXPRESSWAY_ON_THE_LEFT(79),
    EXIT_EXPRESSWAY_ON_THE_RIGHT(80),
    ENTER_MAIN_LINE_ON_THE_LEFT(81),
    ENTER_MAIN_LINE_ON_THE_RIGHT(82),
    PASSING_POINT(87),
    DESTINATION(88),
    STRAIGHT_AT_ROUNDABOUT(91),
    U_TURN_AT_ROUNDABOUT(92),
    LEFT_AT_7_O_CLOCK_AT_ROUNDABOUT(93),
    LEFT_AT_8_O_CLOCK_AT_ROUNDABOUT(94),
    LEFT_AT_9_O_CLOCK_AT_ROUNDABOUT(95),
    LEFT_AT_10_O_CLOCK_AT_ROUNDABOUT(96),
    LEFT_AT_11_O_CLOCK_AT_ROUNDABOUT(97),
    CLOCK_12_AT_ROUNDABOUT(98),
    RIGHT_AT_1_O_CLOCK_AT_ROUNDABOUT(99),
    RIGHT_AT_2_O_CLOCK_AT_ROUNDABOUT(100),
    RIGHT_AT_3_O_CLOCK_AT_ROUNDABOUT(101),
    RIGHT_AT_4_O_CLOCK_AT_ROUNDABOUT(102),
    RIGHT_AT_5_O_CLOCK_AT_ROUNDABOUT(103),
    CLOCK_6_AT_ROUNDABOUT(104),
    TOLL_GATE(121),
    HIGH_PASS_ONLY_TOLL_GATE(122),
    ONE_TOLL_PAY_SYSTEM_TOLL_GATE(123);

    private final int value;

    GuideCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static GuideCode findByValue(int value) {
        for (GuideCode code : values()) {
            if (code.getValue() == value) {
                return code;
            }
        }
        return null;
    }
}
