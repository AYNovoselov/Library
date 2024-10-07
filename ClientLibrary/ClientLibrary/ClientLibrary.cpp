#include <iostream>
#include <curl/curl.h>
#include <string>
#include <json/json.h>

// Функция обратного вызова для записи данных
size_t WriteCallback(void* contents, size_t size, size_t nmemb, std::string* userp) {
    userp->append((char*)contents, size * nmemb);
    return size * nmemb;
}

// Функция для инициализации cURL
CURL* initCurl(const std::string& url, std::string* readBuffer) {
    CURL* curl = curl_easy_init();
    if (curl) {
        curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, readBuffer);
    }
    return curl;
}

// Функция для выполнения GET
std::string httpGet(const std::string& url) {
    std::string readBuffer;
    CURL* curl = initCurl(url, &readBuffer);
    if (curl) {
        CURLcode res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            std::cerr << "Ошибка GET запроса: " << curl_easy_strerror(res) << std::endl;
        }
        curl_easy_cleanup(curl);
    }
    return readBuffer;
}

// Функция для выполнения POST 
void httpPost(const std::string& url, const std::string& data) {
    std::string readBuffer;
    CURL* curl = initCurl(url, &readBuffer);
    if (curl) {
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, data.c_str());
        CURLcode res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            std::cerr << "Ошибка POST запроса: " << curl_easy_strerror(res) << std::endl;
        }
        curl_easy_cleanup(curl);
    }
}

// Функция для регистрации пользователя
std::string registeUser(const std::string& username) {
    std::string url = "http://localhost:8080/users";
    Json::Value user;
    user["username"] = username;

    Json::StreamWriterBuilder writer;
    std::string requestBody = Json::writeString(writer, user);

    std::string readBuffer;
    CURL* curl = initCurl(url, &readBuffer);
    if (curl) {
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, requestBody.c_str());
        struct curl_slist* headers = curl_slist_append(nullptr, "Content-Type: application/json");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

        CURLcode res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            std::cerr << "Ошибка запроса: " << curl_easy_strerror(res) << std::endl;
        }

        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    }
    return readBuffer;
}

// Просмотр всех книг
void viewAllBooks() {
    std::string getBooksUrl = "http://localhost:8080/books";
    std::string response = httpGet(getBooksUrl);
    std::cout << "Все книги: " << response << std::endl;
}

// Поиск книг по названию
void searchBookByTitle() {
    std::string title;
    std::cout << "Введите название книги: ";
    std::cin.ignore();
    std::getline(std::cin, title);

    CURL* curl = curl_easy_init();
    if (!curl) {
        std::cerr << "Ошибка инициализации cURL." << std::endl;
        return;
    }

    char* encodedTitle = curl_easy_escape(curl, title.c_str(), title.length());
    std::string searchUrl = "http://localhost:8080/search/title?title=" + std::string(encodedTitle);

    curl_free(encodedTitle);
    curl_easy_cleanup(curl);

    std::string response = httpGet(searchUrl);
    std::cout << "Книги: " << response << std::endl;
}


// Поиск книг по автору
void searchBookByAuthor() {
    std::string author;
    std::cout << "Введите автора: ";
    std::cin.ignore();
    std::getline(std::cin, author);

    CURL* curl = curl_easy_init();

    char* encodedAuthor = curl_easy_escape(curl, author.c_str(), author.length());
    std::string searchUrl = "http://localhost:8080/search/author?author=" + std::string(encodedAuthor);

    curl_free(encodedAuthor);
    curl_easy_cleanup(curl);

    std::string response = httpGet(searchUrl);
    std::cout << "Книги автора: " << response << std::endl;
}


// Регистрация нового пользователя
void registerUser() {
    std::string username;
    std::cout << "Введите имя пользователя: ";
    std::cin >> username;
    std::string response = registeUser(username);
    std::cout << "Ответ сервера: " << response << std::endl;
}

// Взятие книги напрокат
void borrowBook() {
    int userId, bookId;
    std::cout << "Введите ID пользователя: ";
    std::cin >> userId;
    std::cout << "Введите ID книги: ";
    std::cin >> bookId;
    std::string borrowUrl = "http://localhost:8080/borrow/" + std::to_string(userId) + "/" + std::to_string(bookId);
    httpPost(borrowUrl, "");
    std::cout << "Книга взята. ID пользователя: " << userId << ", ID книги: " << bookId << std::endl;
}

// Возврат книги
void returnBook() {
    int userId, bookId;
    std::cout << "Введите ID пользователя: ";
    std::cin >> userId;
    std::cout << "Введите ID книги: ";
    std::cin >> bookId;
    std::string returnUrl = "http://localhost:8080/return/" + std::to_string(userId) + "/" + std::to_string(bookId);
    httpPost(returnUrl, "");
    std::cout << "Книга возвращена." << std::endl;
}

// Меню 
void showMenu() {
    std::cout << "\nБиблиотека\n";
    std::cout << "1. Просмотр списка всех книг\n";
    std::cout << "2. Поиск книги по названию\n";
    std::cout << "3. Поиск книги по автору\n";
    std::cout << "4. Регистрация нового пользователя\n";
    std::cout << "5. Взятие книги\n";
    std::cout << "6. Возврат книги\n";
    std::cout << "0. Выход\n";
}

int main() {
    setlocale(LC_ALL, "Russian");

    int choice;
    do {
        showMenu();
        std::cout << "Выберите: ";
        std::cin >> choice;
        switch (choice) {
        case 1:
            viewAllBooks();
            break;
        case 2:
            searchBookByTitle();
            break;
        case 3:
            searchBookByAuthor();
            break;
        case 4:
            registerUser();
            break;
        case 5:
            borrowBook();
            break;
        case 6:
            returnBook();
            break;
        case 0:
            std::cout << "Выход...\n";
            break;
        default:
            std::cout << "Неверный выбор. Введите еще раз.\n";
        }
    } while (choice != 0);

    return 0;
}


