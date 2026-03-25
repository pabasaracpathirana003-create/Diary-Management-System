-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 21, 2026 at 07:33 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `diary_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(50) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`, `user_id`) VALUES
(1, 'Personal', NULL),
(2, 'Work', NULL),
(3, 'Health', NULL),
(4, 'Travel', NULL),
(5, 'Study', NULL),
(8, 'Hobiies', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `diary_entries`
--

CREATE TABLE `diary_entries` (
  `entry_id` int(11) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `entry_date` date DEFAULT NULL,
  `mood` varchar(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `diary_entries`
--

INSERT INTO `diary_entries` (`entry_id`, `title`, `description`, `entry_date`, `mood`, `user_id`, `category_id`) VALUES
(1, 'Chasing Sunsets', 'The road stretched endlessly ahead, and the breeze carried whispers of adventure. Every turn revealed a new corner of beauty—sunlight dancing on the water, streets buzzing with laughter, and colors I didn’t know I’d forgotten existed. I felt like a traveler in someone else’s dream, yet every heartbeat reminded me I was exactly where I needed to be.hey', '2026-03-20', 'Happy', 1, 4),
(2, 'Fading Footsteps', 'Today didn’t feel like a journey—it felt like something slowly slipping away. The same roads I once admired now seemed distant and unfamiliar. The sky was quiet, almost like it understood the heaviness in my heart. I walked without direction, carrying thoughts I couldn’t leave behind. Even in a new place, I realized some feelings travel with me, no matter how far I go.', '2026-10-03', 'Sad', 3, 4),
(3, 'Silent Evenings', 'The sunset faded too quickly today, leaving behind a quiet that felt heavier than usual. I sat alone, watching the sky lose its colors, wishing I could hold onto something that was already gone. The world around me moved on, but my heart stayed stuck in a moment I couldn’t change. Sometimes, even the most beautiful places can’t hide the loneliness you carry inside....', '2026-05-20', 'Angry', 1, 1),
(4, 'Sunshine and Smiles', 'Today was magical! The sun was shining, the streets were lively, and I felt completely free as I wandered through the colorful markets. Every corner I turned brought a new surprise, and I couldn’t stop smiling. Travel truly makes my heart happy.', '2026-01-10', 'Happy', 3, 4),
(5, 'Missing Home on the Road', 'Although I’m in a beautiful city, I can’t shake the feeling of homesickness. Everything feels a little lonely without familiar faces. Even the scenery doesn’t seem to cheer me up today', '2026-01-11', 'Sad', 3, 4),
(6, 'Feeling Under the Weather', 'I can’t believe I’m finally here! The adventures I’ve dreamed about for months are happening. I’m buzzing with energy, ready to explore every hidden alley and secret spot this city has to offer', '2026-01-20', 'Excited', 3, 3),
(7, 'Self-Care Victory', 'I feel great today! My energy levels are high, and I managed to complete my workout with ease. Taking care of myself feels rewarding and empowering', '2026-02-01', 'Happy', 3, 3),
(8, 'Productive and Proud', 'Work was surprisingly satisfying today. I completed a challenging project and even got praise from my team. I feel accomplished and motivated to keep performing well.', '2026-01-03', 'Happy', 3, 2),
(9, 'Office Frustrations', 'Deadlines, misunderstandings, and office politics—today was exhausting. I can’t help feeling angry at how stressful work can be. I need a moment to breathe and reset.', '2026-01-05', 'Angry', 3, 2),
(10, 'Discovered Something New!', 'I discovered something fascinating in class today! My curiosity is fully awakened, and I can’t wait to dive deeper into this subject. Learning is such an adventure when you’re excited!', '2026-01-25', 'Excited', 3, 5),
(11, 'Career Milestone', 'Travel today was a nightmare. My flight was delayed, my luggage got lost, and nothing seems to be going right. I just want to scream, but all I can do is wait and hope things improve', '2026-01-09', 'Angry', 3, 5),
(12, 'Energized & Motivated', 'I felt lonely today. It seems like everyone is busy with their own lives, and I’m left in my thoughts. I wish I could shake off this sadness and feel connected again', '2026-01-27', 'Sad', 3, 1),
(13, 'Energized & Motivated', 'Work was surprisingly satisfying today. I completed a challenging project and even got praise from my team. I feel accomplished and motivated to keep performing well.', '2026-02-25', 'Happy', 3, 2),
(14, 'Energized & Motivated', 'I can’t believe I injured myself today. I was so careful, but something went wrong. I’m frustrated with this setback, but I’ll have to stay patient and recover.', '2026-03-04', 'Angry', 3, 2);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `email`) VALUES
(1, 'pabasara', 'paba123', NULL),
(2, 'Arunodi Pathirana', 'arunodi123', 'arunodi@gmail.com'),
(3, 'Tharushika', 'tharu123', 'tharushika@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `diary_entries`
--
ALTER TABLE `diary_entries`
  ADD PRIMARY KEY (`entry_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `diary_entries`
--
ALTER TABLE `diary_entries`
  MODIFY `entry_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `diary_entries`
--
ALTER TABLE `diary_entries`
  ADD CONSTRAINT `diary_entries_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `diary_entries_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
