package com.linkedin.coral.converters;

import com.linkedin.coral.hive.hive2rel.HiveMetastoreClient;
import com.linkedin.coral.hive.hive2rel.HiveToRelConverter;
import com.linkedin.coral.presto.rel2presto.RelToPrestoConverter;
import java.io.File;
import org.apache.calcite.rel.RelNode;

import static com.google.common.base.Preconditions.*;


public class HiveToPrestoConverter {

  private final HiveToRelConverter hiveToRelConverter;
  private final RelToPrestoConverter relToPrestoConverter;

  public HiveToPrestoConverter create(File hiveConfPath) {
    checkNotNull(hiveConfPath);
    HiveToRelConverter hiveToRelConverter = HiveToRelConverter.create(hiveConfPath);
    RelToPrestoConverter relToPrestoConverter = new RelToPrestoConverter();
    return new HiveToPrestoConverter(hiveToRelConverter, relToPrestoConverter);
  }

  public static HiveToPrestoConverter create(HiveMetastoreClient mscClient) {
    checkNotNull(mscClient);
    HiveToRelConverter hiveToRelConverter = HiveToRelConverter.create(mscClient);
    RelToPrestoConverter relToPrestoConverter = new RelToPrestoConverter();
    return new HiveToPrestoConverter(hiveToRelConverter, relToPrestoConverter);
  }

  private HiveToPrestoConverter(HiveToRelConverter hiveToRelConverter,
      RelToPrestoConverter relToPrestoConverter) {
    this.hiveToRelConverter = hiveToRelConverter;
    this.relToPrestoConverter = relToPrestoConverter;
  }

  /**
   * Converts input HiveQL to Presto SQL
   *
   * @param hiveSql hive sql query string
   * @return presto sql string representing input hiveSql
   */
  public String toPrestoSql(String hiveSql) {
    RelNode rel = hiveToRelConverter.convert(hiveSql);
    return relToPrestoConverter.convert(rel);
  }
}