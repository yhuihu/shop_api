package tk.mybatis.mapper;


import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Tiger
 * @date 2019-11-19
 * @see tk.mybatis.mapper
 **/
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
