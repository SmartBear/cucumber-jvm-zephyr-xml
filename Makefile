SHELL := /usr/bin/env bash
VERSION = $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout 2> /dev/null)
NEW_VERSION = $(subst -SNAPSHOT,,$(VERSION))
CURRENT_BRANCH = $(shell git rev-parse --abbrev-ref HEAD)

default:
	@echo "Please read README.md for details about how to release"
	@echo "VERSION: $(VERSION)"
	@echo "NEW_VERSION: $(NEW_VERSION)"
	@echo "CURRENT_BRANCH: $(CURRENT_BRANCH)"
.PHONY: default

docker-run-with-secrets:
	[ -d '../secrets' ] || git clone keybase://team/cucumberbdd/secrets ../secrets
	git -C ../secrets pull
	../secrets/update_permissions
	docker pull cucumber/cucumber-build:latest
	docker run \
	  --volume "${shell pwd}":/app \
	  --volume "${shell pwd}/../secrets/import-gpg-key.sh":/home/cukebot/import-gpg-key.sh \
	  --volume "${shell pwd}/../secrets/codesigning.key":/home/cukebot/codesigning.key \
	  --volume "${shell pwd}/../secrets/.ssh":/home/cukebot/.ssh \
	  --volume "${HOME}/.m2/repository":/home/cukebot/.m2/repository \
	  --volume "${HOME}/.gitconfig":/home/cukebot/.gitconfig \
	  --env-file ../secrets/secrets.list \
	  --user 1000 \
	  --rm \
	  -it cucumber/cucumber-build:latest \
	  bash
.PHONY: docker-run-with-secrets

release:
	[ -f '/home/cukebot/import-gpg-key.sh' ] && /home/cukebot/import-gpg-key.sh
	mvn --batch-mode release:clean release:prepare
	git checkout "v$(NEW_VERSION)"
	mvn deploy -Psign-source-javadoc -DskipTests=true -DskipITs=true -Darchetype.test.skip=true
	git checkout $(CURRENT_BRANCH)
	git fetch
.PHONY: release
