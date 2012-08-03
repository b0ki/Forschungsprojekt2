################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/Helper.cpp \
../src/ImageMatch.cpp \
../src/RobustMatcher.cpp \
../src/main.cpp 

OBJS += \
./src/Helper.o \
./src/ImageMatch.o \
./src/RobustMatcher.o \
./src/main.o 

CPP_DEPS += \
./src/Helper.d \
./src/ImageMatch.d \
./src/RobustMatcher.d \
./src/main.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I/usr/local/Cellar/opencv/2.3.1a/include/opencv -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


