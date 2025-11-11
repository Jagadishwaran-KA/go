package com.example.warehouse_go.models

data class Receive(
    val receiptNo: String,
    val orderNo: String,
    val assignedDate: String,
    val assignedTime: String
)

val receiveCards = listOf(
    Receive("RCPT-1001", "ORD-5001", "2025-01-11", "11:10 PM"),
    Receive("RCPT-1002", "ORD-5002", "2025-01-12", "12:30 PM"),
    Receive("RCPT-1003", "ORD-5003", "2025-01-13", "01:15 PM"),
    Receive("RCPT-1004", "ORD-5004", "2025-01-14", "02:45 PM"),
    Receive("RCPT-1005", "ORD-5005", "2025-01-15", "03:20 PM"),
    Receive("RCPT-1006", "ORD-5006", "2025-01-16", "04:50 PM"),
    Receive("RCPT-1007", "ORD-5007", "2025-01-17", "05:30 PM"),
    Receive("RCPT-1008", "ORD-5008", "2025-01-18", "06:10 PM"),
    Receive("RCPT-1009", "ORD-5009", "2025-01-19", "07:00 PM"),
    Receive("RCPT-1010", "ORD-5010", "2025-01-20", "08:25 PM"),
    Receive("RCPT-1011", "ORD-5011", "2025-01-21", "09:15 PM"),
    Receive("RCPT-1012", "ORD-5012", "2025-01-22", "10:05 PM"),
    Receive("RCPT-1013", "ORD-5013", "2025-01-23", "11:50 PM"),
    Receive("RCPT-1014", "ORD-5014", "2025-01-24", "12:10 PM"),
    Receive("RCPT-1015", "ORD-5015", "2025-01-25", "01:40 PM"),
)
