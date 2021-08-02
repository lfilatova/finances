.PHONY: dev \
		build \
		test \
		clean \


build:
	lein do clean, fig:build

test:
	lein fig:test

dev:
	lein fig:dev

clean:
	lein clean