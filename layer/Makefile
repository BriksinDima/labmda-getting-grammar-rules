# layer/Makefile

.PHONY: build-CommonLayer clean-CommonLayer

# Собираем common и копируем JAR в /java/lib
build-CommonLayer:
	@echo "Building common module via Maven..."
	# запускаем Maven из корня репо, собираем только common (и его зависимости)
	mvn -q -f ../pom.xml -pl common -am -DskipTests package
	@echo "Locating common JAR..."
	# берём любой jar (кроме sources/javadoc)
	JAR=$$(ls -1 ../common/target/*.jar 2>/dev/null | grep -vE '(sources|javadoc)' | head -n1); \
	if [ -z "$$JAR" ]; then \
		echo "ERROR: common JAR not found in ../common/target"; \
		exit 2; \
	fi; \
	echo "Found: $$JAR"; \
	mkdir -p "$(ARTIFACTS_DIR)/java/lib"; \
	cp "$$JAR" "$(ARTIFACTS_DIR)/java/lib/"

clean-CommonLayer:
	@echo "Cleaning layer artifacts in $(ARTIFACTS_DIR)"
	rm -rf "$(ARTIFACTS_DIR)/java"