package org.grouplens.lenskit.eval.util.table;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: schang
 * Date: 5/16/12
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultTable extends AbstractList<Row> implements Table {
    private ArrayList<Row> table;
    private HashMap<String, Integer> header;

    public ResultTable(){
        super();
        table = new ArrayList<Row>();
        header = new HashMap<String, Integer>();
    }

    public ResultTable(HashMap<String, Integer> hdr){
        this();
        this.header = hdr;
    }

    public void setHeader(List<String> hdr){
        for(int i = 0; i < hdr.size(); i++) {
            header.put(hdr.get(i), i);
        }
    }

    @Override
    public ResultTable filter(final String col, final Object data) {
        Predicate<Row> pred = new Predicate<Row>() {
            @Override
            public boolean apply(@Nullable Row input) {
                return  data.equals(input.value(col));
            }
        };
        ResultTable newTable = new ResultTable(this.header);
        Iterable<Row> filtered = Iterables.filter(this.table, pred);
        for(Row row: filtered) {
            newTable.add(row);
        }
        return newTable;
    }

    /**
     * Put a new algorithm in the result.
     *
     * @param list the list of objects to insert to the result table
     */
    public void addResultRow(Object[] list)  {
        ResultRow row = new ResultRow(header, list);
        table.add(row);
    }

    @Override
    public int size() {
        return table.size();
    }

    @Override
    public Iterator<Row> iterator() {
        return table.iterator();
    }

    @Override
    public boolean add(Row row) {
        return table.add(row);
    }

    @Override
    public boolean remove(Object o) {
        return table.remove(o);
    }

    @Override
    public Row get(int i) {
        return table.get(i);
    }

    @Override
    public Row remove(int i) {
        return table.remove(i);
    }

    public ResultColumn column(String col){
        return new ResultColumn(col);
    }

    public class ResultColumn extends AbstractList<Object> implements Column{
        ArrayList<Object> column;

        ResultColumn(String col) {
            super();
            column = new ArrayList<Object>();;
            if(header.get(col)!=null){
                for(Row row : table) {
                    column.add(row.value(header.get(col)));
                }
            }
        }

        /**
         * Return all the values in the specified column
         * @return  An array of the values
         */
        public Object[] values() {
            return column.toArray();
        }

        public Double sum() {
            double sum = 0;
            if(column.size() == 0 ||
                    !Number.class.isAssignableFrom(column.get(0).getClass())) {
                return Double.NaN;
            }
            else {
                for(Object v : column) {
                    sum += ((Number)v).doubleValue();
                }
                return sum;
            }
        }

        public Double average() {
            if(column.size()==0)
                return Double.NaN;
            return sum()/column.size();
        }
        @Override
        public int size() {
            return column.size();  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object get(int i) {
            return column.get(i);  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}