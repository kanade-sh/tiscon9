package com.tiscon.dao;

import com.tiscon.domain.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * 引越し見積もり機能においてDBとのやり取りを行うクラス。
 *
 * @author Oikawa Yumi
 */
@Component
public class EstimateDao {

    /** データベース・アクセスAPIである「JDBC」を使い、名前付きパラメータを用いてSQLを実行するクラス */
    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    /**
     * コンストラクタ。
     *
     * @param parameterJdbcTemplate NamedParameterJdbcTemplateクラス
     */
    public EstimateDao(NamedParameterJdbcTemplate parameterJdbcTemplate) {
        this.parameterJdbcTemplate = parameterJdbcTemplate;
    }

    /**
     * 顧客テーブルに登録する。
     *
     * @param customer 顧客情報
     * @return 登録件数
     */
    public int insertCustomer(Customer customer) {
        String sql = "INSERT INTO CUSTOMER(OLD_PREFECTURE_ID, NEW_PREFECTURE_ID, CUSTOMER_NAME, TEL, EMAIL, OLD_ADDRESS, NEW_ADDRESS)"
                + " VALUES(:oldPrefectureId, :newPrefectureId, :customerName, :tel, :email, :oldAddress, :newAddress)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int resultNum = parameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(customer), keyHolder);
        customer.setCustomerId(keyHolder.getKey().intValue());
        return resultNum;
    }

    /**
     * オプションサービス_顧客テーブルに登録する。
     *
     * @param optionService オプションサービス_顧客に登録する内容
     * @return 登録件数
     */
    public int insertCustomersOptionService(CustomerOptionService optionService) {
        String sql = "INSERT INTO CUSTOMER_OPTION_SERVICE(CUSTOMER_ID, SERVICE_ID)"
                + " VALUES(:customerId, :serviceId)";
        return parameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(optionService));
    }

    /**
     * 顧客_荷物テーブルに登録する。
     *
     * @param packages 登録する荷物
     * @return 登録件数
     */
    public int[] batchInsertCustomerPackage(List<CustomerPackage> packages) {
        String sql = "INSERT INTO CUSTOMER_PACKAGE(CUSTOMER_ID, PACKAGE_ID, PACKAGE_NUMBER)"
                + " VALUES(:customerId, :packageId, :packageNumber)";
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(packages.toArray());

        return parameterJdbcTemplate.batchUpdate(sql, batch);
    }

    /**
     * 都道府県テーブルに登録されているすべての都道府県を取得する。
     *
     * @return すべての都道府県
     */
    public List<Prefecture> getAllPrefectures() {
        String sql = "SELECT PREFECTURE_ID, PREFECTURE_NAME FROM PREFECTURE";
        return parameterJdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Prefecture.class));
    }

    /**
     * 都道府県間の距離を取得する。
     *
     * @param prefectureIdFrom 引っ越し元の都道府県
     * @param prefectureIdTo   引越し先の都道府県
     * @return 距離[km]
     */
    public double getDistance(String prefectureIdFrom, String prefectureIdTo) {
        // 都道府県のFromとToが逆転しても同じ距離となるため、「そのままの状態のデータ」と「FromとToを逆転させたデータ」をくっつけた状態で距離を取得する。
        String sql = "SELECT DISTANCE FROM (" +
                "SELECT PREFECTURE_ID_FROM, PREFECTURE_ID_TO, DISTANCE FROM PREFECTURE_DISTANCE UNION ALL " +
                "SELECT PREFECTURE_ID_TO PREFECTURE_ID_FROM ,PREFECTURE_ID_FROM PREFECTURE_ID_TO ,DISTANCE FROM PREFECTURE_DISTANCE) " +
                "WHERE PREFECTURE_ID_FROM  = :prefectureIdFrom AND PREFECTURE_ID_TO  = :prefectureIdTo";

        PrefectureDistance prefectureDistance = new PrefectureDistance();
        prefectureDistance.setPrefectureIdFrom(prefectureIdFrom);
        prefectureDistance.setPrefectureIdTo(prefectureIdTo);

        double distance;
        try {
            distance = parameterJdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(prefectureDistance), double.class);
            System.out.println("TEST");
        } catch (IncorrectResultSizeDataAccessException e) {
            distance = 50;
        }
        //TODO: bugfixのため、一時的に対応。将来的には同一都道府県内のデータはDBで管理するようにする。
        if(prefectureIdFrom.equals(prefectureIdTo)) {
            HashMap<String, Integer> samePrefectureDistanceMap = new HashMap<>();
            samePrefectureDistanceMap.put("01", 560);
            samePrefectureDistanceMap.put("02", 205);
            samePrefectureDistanceMap.put("03", 235);
            samePrefectureDistanceMap.put("04", 165);
            samePrefectureDistanceMap.put("05", 205);
            samePrefectureDistanceMap.put("06", 195);
            samePrefectureDistanceMap.put("07", 225);
            samePrefectureDistanceMap.put("08", 135);
            samePrefectureDistanceMap.put("09", 125);
            samePrefectureDistanceMap.put("10", 125);
            samePrefectureDistanceMap.put("11", 90);
            samePrefectureDistanceMap.put("12", 100);
            samePrefectureDistanceMap.put("13", 50);
            samePrefectureDistanceMap.put("14", 60);
            samePrefectureDistanceMap.put("15", 175);
            samePrefectureDistanceMap.put("16", 90);
            samePrefectureDistanceMap.put("17", 110);
            samePrefectureDistanceMap.put("18", 90);
            samePrefectureDistanceMap.put("19", 80);
            samePrefectureDistanceMap.put("20", 195);
            samePrefectureDistanceMap.put("21", 135);
            samePrefectureDistanceMap.put("22", 135);
            samePrefectureDistanceMap.put("23", 125);
            samePrefectureDistanceMap.put("24", 125);
            samePrefectureDistanceMap.put("25", 80);
            samePrefectureDistanceMap.put("26", 70);
            samePrefectureDistanceMap.put("27", 60);
            samePrefectureDistanceMap.put("28", 80);
            samePrefectureDistanceMap.put("29", 50);
            samePrefectureDistanceMap.put("30", 70);
            samePrefectureDistanceMap.put("31", 70);
            samePrefectureDistanceMap.put("32", 80);
            samePrefectureDistanceMap.put("33", 90);
            samePrefectureDistanceMap.put("34", 135);
            samePrefectureDistanceMap.put("35", 90);
            samePrefectureDistanceMap.put("36", 60);
            samePrefectureDistanceMap.put("37", 60);
            samePrefectureDistanceMap.put("38", 80);
            samePrefectureDistanceMap.put("39", 90);
            samePrefectureDistanceMap.put("40", 100);
            samePrefectureDistanceMap.put("41", 70);
            samePrefectureDistanceMap.put("42", 100);
            samePrefectureDistanceMap.put("43", 100);
            samePrefectureDistanceMap.put("44", 90);
            samePrefectureDistanceMap.put("45", 100);
            samePrefectureDistanceMap.put("46", 110);
            samePrefectureDistanceMap.put("47", 165);

            distance = samePrefectureDistanceMap.get(prefectureIdFrom);
        }
        return distance;
    }

    /**
     * 荷物ごとの段ボール数を取得する。
     *
     * @param packageId 荷物ID
     * @return 段ボール数
     */
    public int getBoxPerPackage(int packageId) {
        String sql = "SELECT BOX FROM PACKAGE_BOX WHERE PACKAGE_ID = :packageId";

        SqlParameterSource paramSource = new MapSqlParameterSource("packageId", packageId);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
    }

    /**
     * 段ボール数に応じたトラック料金を取得する。
     *
     * @param boxNum 総段ボール数
     * @return 料金[円]
     */
    public int getPricePerTruck(int boxNum) {
        String sql = "SELECT PRICE FROM TRUCK_CAPACITY WHERE MAX_BOX >= :boxNum ORDER BY PRICE LIMIT 1";

        SqlParameterSource paramSource = new MapSqlParameterSource("boxNum", boxNum);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
    }

    /**
     * オプションサービスの料金を取得する。
     *
     * @param serviceId サービスID
     * @return 料金
     */
    public int getPricePerOptionalService(int serviceId) {
        String sql = "SELECT PRICE FROM OPTIONAL_SERVICE WHERE SERVICE_ID = :serviceId";

        SqlParameterSource paramSource = new MapSqlParameterSource("serviceId", serviceId);
        return parameterJdbcTemplate.queryForObject(sql, paramSource, Integer.class);
    }
}
