-- phpMyAdmin SQL Dump
-- version 2.9.2
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generatie Tijd: 21 Feb 2007 om 21:13
-- Server versie: 5.0.32
-- PHP Versie: 5.1.6-pl8-gentoo
-- 
-- Database: `ontolearn`
-- 

-- --------------------------------------------------------

-- 
-- Tabel structuur voor tabel `association_abstract`
-- 

CREATE TABLE `association_abstract` (
  `id` int(11) NOT NULL auto_increment,
  `document` varchar(255) NOT NULL,
  `word` varchar(255) NOT NULL,
  `wordcount` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2377 ;

-- --------------------------------------------------------