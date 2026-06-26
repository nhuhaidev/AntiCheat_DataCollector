package vn.hai.anticheat;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataLogger {
    // Sử dụng ConcurrentLinkedQueue để Main Thread và Async Thread không bị đụng độ bộ nhớ
    private final ConcurrentLinkedQueue<Object> logQueue = new ConcurrentLinkedQueue<>();
    private final JavaPlugin plugin;
    private final Gson gson = new Gson();
    private final File logFile;
    // File logFileAll File lưu tất cả dữ liệu
    private final File logFileAll;
    // Giới hạn số lượng dữ liệu trong Queue để tránh chiếm quá nhiều RAM
    private static final int MAX_QUEUE_SIZE = 50000;

    public DataLogger(JavaPlugin plugin) { 
        this.plugin = plugin;
        
        // Tạo file lưu trữ data.json trong thư mục plugins/DataCollector
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        this.logFile = new File(plugin.getDataFolder(), "raw_server_events.json");
        this.logFileAll = new File(plugin.getDataFolder(), "raw_server_events_all.json");
    }

    //  Hàm này được gọi từ các Listener để đẩy dữ liệu vào Queue
    public void pushData(Object dataRecord) {
        // Cơ chế Backpressure: Nếu Queue đầy, ta chấp nhận hi sinh (drop) dữ liệu cũ hoặc mới 
        // để bảo vệ Server không bị crash.
        // Nếu Queue đã đầy, bỏ qua dữ liệu mới để tránh chiếm quá nhiều RAM
        if (logQueue.size() >= MAX_QUEUE_SIZE) {
            plugin.getLogger().warning("Queue da day, bo qua du lieu moi de tranh chiem RAM.");
            // Có thể log thêm thông tin về dữ liệu bị bỏ qua nếu cần.
            return;
        }
        logQueue.add(dataRecord); 
    }

    // Bắt đầu một luồng bất đồng bộ để ghi dữ liệu từ Queue xuống file
    public void startAsyncLogging() {
        // Sử dụng BukkitRunnable để chạy lặp lại mỗi 5 giây (100 ticks)
        new BukkitRunnable() {
            @Override
            public void run() {
                // Nếu không có ai di chuyển thì bỏ qua
                if (logQueue.isEmpty()) 
                    return; 

                // Rút tối đa một số lượng nhất định để tránh block Async Thread quá lâu
                List<Object> batchToSave = new ArrayList<>();

                int batchSize = 0; 
                while (!logQueue.isEmpty() && batchSize < 10000) {
                    batchToSave.add(logQueue.poll());
                    batchSize++;
                }

                // Try-with-resources đảm bảo file luôn được đóng an toàn
                // Ghi dữ liệu ra file logFileAll (lưu tất cả dữ liệu)
                try (FileWriter fwAll = new FileWriter(logFileAll, true);
                     PrintWriter bwAll = new PrintWriter(fwAll)) {
                    
                    for (Object record : batchToSave) {
                        // Chuyển đổi dữ liệu sang chuỗi JSON và ghi ra file
                        bwAll.println(gson.toJson(record)); 
                    }
                    
                } catch (IOException e) {
                    plugin.getLogger().severe("Lỗi khi ghi file logAll: " + e.getMessage());
                }
                // Ghi dữ liệu ra file logFile (lưu dữ liệu mới nhất)
                try (FileWriter fw = new FileWriter(logFile, true);
                     PrintWriter bw = new PrintWriter(fw)) {
                    
                    for (Object record : batchToSave) {
                        // Chuyển đổi dữ liệu sang chuỗi JSON và ghi ra file
                        bw.println(gson.toJson(record)); 
                    }
                    
                } catch (IOException e) {
                    plugin.getLogger().severe("Lỗi khi ghi file log: " + e.getMessage());
                }
            }
        }
        // 1 giây (20 ticks)
        .runTaskTimerAsynchronously(plugin, 100L, 100L);
    }
    // Ghi tất cả dữ liệu còn lại trong Queue xuống file trước khi server tắt
    public void flushRemainingData() {
        // Nếu Queue trống, không cần ghi gì cả
        if (logQueue.isEmpty()) {
            plugin.getLogger().info("Khong co du lieu ton dong trong Queue.");
            return;
        }
        // Ghi tất cả dữ liệu còn lại trong Queue xuống file logFile
        plugin.getLogger().info("Dang xa (flush) " + logQueue.size() + " records xuong o cung...");
        
        // Try-with-resources đảm bảo file luôn được đóng an toàn
        // Ghi dữ liệu ra file logFile (lưu dữ liệu mới nhất)
        try (FileWriter fw = new FileWriter(logFile, true);
             PrintWriter bw = new PrintWriter(fw)) {
            
            while (!logQueue.isEmpty()) {
                Object record = logQueue.poll();
                bw.println(gson.toJson(record)); 
            }
            
        } catch (IOException e) {
            plugin.getLogger().severe("Loi khi flush file log: " + e.getMessage());
        }
        
        // Ghi dữ liệu ra file logFileAll (lưu tất cả dữ liệu)
        try (FileWriter fwAll = new FileWriter(logFileAll, true);
             PrintWriter bwAll = new PrintWriter(fwAll)) {
            
            while (!logQueue.isEmpty()) {
                Object record = logQueue.poll();
                bwAll.println(gson.toJson(record)); 
            }
            
        } catch (IOException e) {
            plugin.getLogger().severe("Loi khi flush file logAll: " + e.getMessage());
        }
    }
}