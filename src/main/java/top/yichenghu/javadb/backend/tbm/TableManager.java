package top.yichenghu.javadb.backend.tbm;

import top.yichenghu.javadb.backend.dm.DataManager;
import top.yichenghu.javadb.backend.parser.statement.Begin;
import top.yichenghu.javadb.backend.parser.statement.Create;
import top.yichenghu.javadb.backend.parser.statement.Delete;
import top.yichenghu.javadb.backend.parser.statement.Insert;
import top.yichenghu.javadb.backend.parser.statement.Select;
import top.yichenghu.javadb.backend.parser.statement.Update;
import top.yichenghu.javadb.backend.utils.Parser;
import top.yichenghu.javadb.backend.vm.VersionManager;

public interface TableManager {
    BeginRes begin(Begin begin);
    byte[] commit(long xid) throws Exception;
    byte[] abort(long xid);

    byte[] show(long xid);
    byte[] create(long xid, Create create) throws Exception;

    byte[] insert(long xid, Insert insert) throws Exception;
    byte[] read(long xid, Select select) throws Exception;
    byte[] update(long xid, Update update) throws Exception;
    byte[] delete(long xid, Delete delete) throws Exception;

    public static TableManager create(String path, VersionManager vm, DataManager dm) {
        Booter booter = Booter.create(path);
        booter.update(Parser.long2Byte(0));
        return new TableManagerImpl(vm, dm, booter);
    }

    public static TableManager open(String path, VersionManager vm, DataManager dm) {
        Booter booter = Booter.open(path);
        return new TableManagerImpl(vm, dm, booter);
    }
}
