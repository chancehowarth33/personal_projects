# Web Scraping Algorithims

MatrixSearcher: This class is designed to navigate through data structured as an adjacency matrix within a pandas DataFrame. It is not directly related to web scraping, but it could be used to analyze and traverse a graph-like structure, potentially representing web link connections or site architecture.

FileSearcher: This class focuses on reading and traversing through a file-based system, extracting linked data from files. While not inherently a web scraping tool, it could be utilized to process and navigate through locally saved web content or site map structures stored in files.

WebSearcher: Specifically tailored for web scraping, this class uses a Selenium WebDriver to navigate web pages. It extracts and compiles data from these pages, particularly focusing on scraping tables and following hyperlinks to traverse through connected web pages.

FileSearcher: This class is specifically designed for navigating and processing a graph-like structure represented through files in a directory. It reads nodes from individual files, with each file potentially containing links to other nodes in the form of file names. 
Primarily used for traversing and extracting data from a file system, it's ideal for scenarios where the relationship between different elements is represented through file connections, resembling the hyperlink structure of websites but in a local file system context.
