# Unique IP Tracker

![build status badge](https://github.com/brzzznko/UniqueIpTracker/actions/workflows/build.yml/badge.svg)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/63887cb1260f40f3bce2b33ddc3736df)](https://app.codacy.com/gh/brzzznko/UniqueIpTracker/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/63887cb1260f40f3bce2b33ddc3736df)](https://app.codacy.com/gh/brzzznko/UniqueIpTracker/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)

## **Overview**
The Unique IP Tracker is a high-performance tool for processing large files and counting unique IP addresses. It supports multiple processing methods and is optimized for parallel execution.

---

## **Algorithm**

The Unique IP Tracker efficiently processes large files using bit operations and parallel processing techniques:

1. Bit Array Representation: Uses a large bit array (`AtomicBitArrayIpTracker.class`) to store seen IP addresses efficiently in memory.
2. IP to Long Conversion: Converts IPv4 addresses into a single long value to map them into the bit array.
3. Parallel Processing Methods:
   - Parallel Streams (default) - Uses Java's built-in parallel streams for high-speed processing. (most efficient)
   - Chunks - Splits the file into chunks and processes them concurrently.
   - Single-threaded - Processes line by line without parallel execution for minimal memory usage. 
4. Efficient I/O Handling: Streams the file without loading it fully into memory to support very large files.
5. Bitwise Operations for Fast Lookups: Uses bitwise shifts and masks to set and check bits in the bit array, reducing memory overhead compared to HashSets.

## **Features**
- **Parallel Processing** - Uses Parallel Streams, multi-threading for fast execution.  
- **Customizable Processing Modes** - Choose from `parallel-streams`, `chunks`, or `single-threaded`.  
- **Memory Optimized** - Uses bit operations and bit arrays to efficiently track unique IPs while minimizing memory usage.  
- **Command-Line Interface** - Easily select processing mode and input file.  
- **Pre-built JAR Available** - Download from [GitHub Releases](https://github.com/brzzznko/unique-ip-tracker/releases).

---

## **Installation & Setup**

### **Clone the Repository**
```sh
git clone https://github.com/YOUR_GITHUB_USERNAME/unique-ip-tracker.git
cd unique-ip-tracker
```

### **Build the Project**
```sh
./gradlew clean shadowJar
```
This generates a **fat JAR** in `build/libs/` containing all dependencies.

Alternatively, download the latest **pre-built JAR** from [GitHub Releases](https://github.com/YOUR_GITHUB_USERNAME/unique-ip-tracker/releases) and skip the build step.

---

## **Usage**

### **Example Input File (`example.txt`)**
```
97.71.174.4
97.71.173.241
97.71.173.235
161.71.174.27
215.10.61.107
```

### **Run Locally**
```sh
java -jar build/libs/unique-ip-tracker-1.0.0.jar --processor=parallel-streams --filename=path/input.txt
```
✅ **Available Processors:** `parallel-streams` (default), `chunks`, `single-threaded`

### **Run in Docker**
```sh
docker build -t unique-ip-tracker .  
docker run --rm  -v ${PWD}/ip_addresses:/app/example.txt unique-ip-tracker
```
