package vn.hai.anticheat;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
//import java.util.HashMap;
//simport java.util.Map;

public class PlayerMovementListener implements Listener {

    private final DataLogger logger;

    // Constructor để nhận DataLogger từ plugin chính
    public PlayerMovementListener( DataLogger logger) {
        this.logger = logger;
    }

    // Class POJO định nghĩa dữ liệu di chuyển của người chơi
    public static class MoveDataLog {
        public String event_type = "move";
        public long timestamp;
        public String uuid;
        public double x, y, z;
        public float yaw, pitch;
        public boolean is_on_ground;

        public MoveDataLog(long timestamp, String uuid, Location loc, boolean isOnGround) {
            this.timestamp = timestamp;
            this.uuid = uuid;
            this.x = loc.getX();
            this.y = loc.getY();
            this.z = loc.getZ();
            this.yaw = loc.getYaw();
            this.pitch = loc.getPitch();
            this.is_on_ground = isOnGround;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        // Bỏ qua nếu dữ liệu vị trí bị rỗng hoặc người chơi chỉ xoay chuột (không đổi tọa độ xyz)
        if (to == null) return;

        Player player = event.getPlayer();
        
        // 1. Khởi tạo Object trực tiếp
        MoveDataLog logEntry = new MoveDataLog(
                System.currentTimeMillis(),
                player.getUniqueId().toString(),
                to,
                player.isOnGround() // Bổ sung cờ trạng thái mặt đất
        );

        // 2. Gửi dữ liệu sang DataLogger 
        logger.pushData(logEntry);
    }
}
