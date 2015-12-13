import com.purbon.jruby.monitor.HotThreads;
import com.purbon.jruby.monitor.JRubyUtils;
import org.jruby.*;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.List;
import java.util.Map;

/**
 * Created by purbon on 12/12/15.
 */
@JRubyClass(name = "ThreadsReport", parent = "Object")
public class ThreadsReport extends RubyObject {

    public ThreadsReport(Ruby ruby, RubyClass metaclass) {
        super(ruby, metaclass);
    }

    /**
     * Build a report with current Thread information
     * @param context
     * @param self
     * @return
     */
    @JRubyMethod(module = true, name = { "build" })
    public static RubyHash build(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;

        RubyHash hash = new RubyHash(runtime);

        HotThreads reporter = new HotThreads();
        List<HotThreads.ThreadReport> reports = reporter.detect();

        for(HotThreads.ThreadReport report : reports) {
             RubyHash reportHash = JRubyUtils.toRubyHash(runtime, report.toHash());
            hash.put(report.getThreadName(), reportHash);
        }
        return hash;
    }

    /**
     * Rreturn the report as string
     * @param context
     * @param self
     * @return
     */
    @JRubyMethod(module = true, name = { "to_s" })
    public static RubyString to_string(ThreadContext context, IRubyObject self) {
        Ruby runtime = context.runtime;

        StringBuilder sb = new StringBuilder();

        HotThreads reporter = new HotThreads();
        List<HotThreads.ThreadReport> reports = reporter.detect();

        for (HotThreads.ThreadReport report : reports) {
            sb.append(report);
            sb.append("\n");
        }
        return runtime.newString(sb.toString());
    }
}

