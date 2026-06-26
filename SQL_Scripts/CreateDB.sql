-- 1. Tạo Database
CREATE DATABASE AntiCheatDB;
GO
USE AntiCheatDB;
GO

-- 2. Bảng lưu dữ liệu Di chuyển (Movement)
CREATE TABLE Log_Movement (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    uuid VARCHAR(50) NOT NULL,
    timestamp BIGINT NOT NULL,
    x FLOAT NOT NULL,
    y FLOAT NOT NULL,
    z FLOAT NOT NULL,
    yaw FLOAT NOT NULL,
    pitch FLOAT NOT NULL
);
GO

-- 3. Bảng lưu dữ liệu Chiến đấu (Combat)
CREATE TABLE Log_Combat (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    uuid VARCHAR(50) NOT NULL,
    timestamp BIGINT NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    distance FLOAT NOT NULL,
    damage_dealt FLOAT NOT NULL
);
GO

-- 4. Bảng lưu dữ liệu Đào khối (Mining)
CREATE TABLE Log_Mining (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    uuid VARCHAR(50) NOT NULL,
    timestamp BIGINT NOT NULL,
    block_type VARCHAR(50) NOT NULL,
    block_x INT NOT NULL,
    block_y INT NOT NULL,
    block_z INT NOT NULL,
    light_level INT NOT NULL
);
GO

-- 5. Bảng lưu dữ liệu Túi đồ (Inventory)
CREATE TABLE Log_Inventory (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    uuid VARCHAR(50) NOT NULL,
    timestamp BIGINT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    slot_clicked INT NOT NULL,
    is_shift_click BIT NOT NULL -- BIT trong SQL tương đương với Boolean (True/False)
);
GO

-- 6. Tạo Index trên cột uuid và timestamp để tăng tốc độ truy vấn khi làm Feature Engineering
CREATE NONCLUSTERED INDEX IX_Movement_UUID_Time ON Log_Movement(uuid, timestamp);
CREATE NONCLUSTERED INDEX IX_Combat_UUID_Time ON Log_Combat(uuid, timestamp);
CREATE NONCLUSTERED INDEX IX_Mining_UUID_Time ON Log_Mining(uuid, timestamp);
CREATE NONCLUSTERED INDEX IX_Inventory_UUID_Time ON Log_Inventory(uuid, timestamp);
GO
EXEC sp_MSforeachtable 'DELETE FROM ?';