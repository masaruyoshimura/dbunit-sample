package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class) // DBUnitでCSVファイルを使えるよう指定。＊CsvDataSetLoaderクラスは自作します（後述）
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
        TransactionDbUnitTestExecutionListener.class // @DatabaseSetupや＠ExpectedDatabaseなどを使えるように指定
})
@Transactional // @DatabaseSetupで投入するデータをテスト処理と同じトランザクション制御とする。（テスト後に投入データもロールバックできるように）
public class DbunitSampleTest {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

    @Test
    @DatabaseSetup("/dbunit/sample")
    public void test() {
    	var actual = jdbcTemplate.queryForList("select id, column1, column2 from sample_table order by id");
// actual の中身
// [{id=1, column1=hoge1, column2=fuga1}, {id=2, column1=hoge2, column2=fuga2}, {id=3, column1=hoge3, column2=fuga3}, {id=4, column1=hoge4, column2=fuga4}]
    	assertEquals(4, actual.size());
    	assertEquals(1, actual.get(0).get("id"));
    	assertEquals("hoge1", actual.get(0).get("column1"));
    	assertEquals("fuga1", actual.get(0).get("column2"));
    }
}
