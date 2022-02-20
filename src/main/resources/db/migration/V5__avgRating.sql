ALTER TABLE `movies`
	ADD COLUMN `avg_rating` DOUBLE NULL DEFAULT NULL AFTER `release_date`;