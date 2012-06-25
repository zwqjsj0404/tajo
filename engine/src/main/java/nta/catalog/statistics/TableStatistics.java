package nta.catalog.statistics;

import nta.catalog.Schema;
import nta.catalog.proto.CatalogProtos;
import nta.datum.Datum;
import nta.datum.DatumType;

/**
 * This class is not thread-safe.
 *
 * @author Hyunsik Choi
 */
public class TableStatistics {
  private Schema schema;
  private long [] minValues;
  private long [] maxValues;
  private long [] numNulls;
  private long numRows = 0;
  private long numBytes = 0;


  private boolean [] numericFields;

  public TableStatistics(Schema schema) {
    this.schema = schema;
    minValues = new long[schema.getColumnNum()];
    maxValues = new long[schema.getColumnNum()];
    for (int i = 0; i < schema.getColumnNum(); i++) {
      minValues[i] = Long.MAX_VALUE;
      maxValues[i] = Long.MIN_VALUE;
    }

    numNulls = new long[schema.getColumnNum()];
    numericFields = new boolean[schema.getColumnNum()];

    CatalogProtos.DataType type;
    for (int i = 0; i < schema.getColumnNum(); i++) {
      type = schema.getColumn(i).getDataType();
      if (type == CatalogProtos.DataType.CHAR ||
          type == CatalogProtos.DataType.BYTE ||
          type == CatalogProtos.DataType.SHORT ||
          type == CatalogProtos.DataType.INT ||
          type == CatalogProtos.DataType.LONG ||
          type == CatalogProtos.DataType.FLOAT ||
          type == CatalogProtos.DataType.DOUBLE ||
          type == CatalogProtos.DataType.STRING) {
        numericFields[i] = true;
      } else {
        numericFields[i] = false;
      }
    }
  }

  public Schema getSchema() {
    return this.schema;
  }

  public void incrementRow() {
    numRows++;
  }

  public long getNumRows() {
    return this.numRows;
  }

  public void setNumBytes(long bytes) {
    this.numBytes = bytes;
  }

  public long getNumBytes() {
    return this.numBytes;
  }

  public void analyzeField(int idx, Datum datum) {
    if (datum.type() == DatumType.NULL) {
      numNulls[idx]++;
    }

    if (datum.type() != DatumType.ARRAY) {
      if (numericFields[idx]) {
        // TODO - it is ad-hoc way. It should be improved
        if (datum.type() == DatumType.STRING) {
          if (maxValues[idx] < datum.asChars().charAt(0)) {
            maxValues[idx] = datum.asChars().charAt(0);
          }
          if (minValues[idx] > datum.asChars().charAt(0)) {
            minValues[idx] = datum.asChars().charAt(0);
          }
        } else {
          if (maxValues[idx] < datum.asLong()) {
            maxValues[idx] = datum.asLong();
          }
          if (minValues[idx] > datum.asLong()) {
            minValues[idx] = datum.asLong();
          }
        }
      }
    }
  }

  public TableStat getTableStat() {
    TableStat stat = new TableStat();

    ColumnStat columnStat;
    for (int i = 0; i < schema.getColumnNum(); i++) {
      columnStat = new ColumnStat(schema.getColumn(i));
      columnStat.setNumNulls(numNulls[i]);
      columnStat.setMinValue(minValues[i]);
      columnStat.setMaxValue(maxValues[i]);
      stat.addColumnStat(columnStat);
    }

    stat.setNumRows(this.numRows);
    stat.setNumBytes(this.numBytes);

    return stat;
  }
}
