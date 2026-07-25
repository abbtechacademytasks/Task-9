# Task-9

📋 Tapşırıq: "Multi-Branch Library Management System" (sadələşdirilmiş versiya)
🎯 Məqsəd:
Bir neçə filialı olan kitabxana şəbəkəsini modelləşdirin. Kitablar filiallar arasında transfer oluna bilər, üzvlər fərqli filiallardan kitab götürə bilər, sistem bildiriş, cərimə, rezervasiya və hesabat funksiyaları ilə işləməlidir. Exception, generic və stream istifadə olunmayacaq — bütün xətalar boolean qaytarmaqla və ya mesaj çap etməklə idarə olunacaq.

1️⃣ Class / Interface strukturu
Enum-lar
MembershipType → REGULAR, PREMIUM, STUDENT
CopyStatus → AVAILABLE, BORROWED, LOST, IN_TRANSIT
NotificationType → DUE_SOON, OVERDUE, RESERVATION_READY, BLACKLIST_WARNING
Interfeyslər
Finable → double calculateFine(int daysLate);
Notifiable → void receiveNotification(Notification n);
Abstract class
LibraryUser (abstract) → sahələr: id, name, email; abstract metod: double getDiscountRate()
Member bu class-ı extend edəcək
Əsas class-lar (hamısı ayrı fayl kimi düşünülsün)
Book — id, title, author, genre, isbn
BookCopy — copyId, book, branchId, status
Member extends LibraryUser implements Finable, Notifiable
Branch — branchId, name, address
Loan — loanId, bookCopy, member, borrowDay, dueDay, returned (boolean)
Reservation — member, book, reservationDay, priorityScore (int — PREMIUM=1, REGULAR=2, STUDENT=3, kiçik rəqəm = yüksək prioritet)
Notification — type, message, day
FineRecord — member, amount, reason, day
Library — bütün sistemi idarə edən əsas class


2️⃣ Data strukturları
Məqsəd
Struktur
Filiallar (ID → Branch)
Map<String, Branch>
Hər filialın kitab nüsxələri (bookId → copies)
Hər Branch daxilində Map<String, List<BookCopy>>
Bütün sistemdəki unikal kitablar
Set<Book>
Aktiv götürmələr, üzvə görə
Map<Member, List<Loan>>
Bütün Loan-ların tam tarixçəsi
List<Loan>
Kitab üzrə rezervasiya növbəsi (prioritetli)
Map<String, PriorityQueue<Reservation>> (bookId → queue)
Populyarlıq sayğacı
Map<Book, Integer>
Cərimə tarixçəsi
List<FineRecord>
Üzvün bildiriş qutusu
Map<Member, Queue<Notification>>
Qara siyahı
Set<Member>
Filiallararası transfer tarixçəsi (son 5)
Deque<String>
Janr → kitablar (əlifba sırası ilə)
TreeMap<String, Set<Book>>
Ən aktiv üzvlər sayğacı
Map<Member, Integer>





3️⃣ Əməliyyatlar / Logic
Library.borrowBook(Member m, String bookId, String branchId, int currentDay) → boolean qaytarsın
Əgər m qara siyahıdadırsa → console-a "Member is blacklisted!" çap et, false qaytar
Filialda kitabın AVAILABLE statuslu nüsxəsini axtar
Tapılmasa:
"Book not available, added to reservation queue" çap et
Üzvü PriorityQueue<Reservation>-a əlavə et (priorityScore membership-ə görə)
false qaytar
Tapılsa:
Nüsxənin statusu BORROWED et
dueDay = currentDay + 14 (amma PREMIUM üçün +21)
Yeni Loan yarat, siyahılara əlavə et
Populyarlıq sayğacını artır
true qaytar
Library.returnBook(Member m, String loanId, int currentDay)
Uyğun Loan tapılmasa → "Loan not found" çap et, dayan
returned = true et
Əgər currentDay > dueDay:
daysLate = currentDay - dueDay
m.calculateFine(daysLate) çağır (hər membership tipi fərqli məbləğ hesablasın — if-else və ya switch ilə: STUDENT → 0.3/gün, REGULAR → 0.5/gün, PREMIUM → 0.2/gün)
FineRecord yarat, siyahıya əlavə et
Əgər bu üzvün tarixçədəki gecikmə sayı 3-dən çoxdursa → qara siyahıya əlavə et, BLACKLIST_WARNING bildirişi yarat
Kitab boşalanda:
Əgər o kitabın rezervasiya növbəsi boş deyilsə → PriorityQueue-dan poll() et, həmin üzvə avtomatik yeni Loan yarat, RESERVATION_READY bildirişi göndər
Əks halda nüsxənin statusunu AVAILABLE et
Library.transferBook(String bookId, String fromBranchId, String toBranchId) → boolean
fromBranch-da uyğun nüsxə yoxdursa → "Transfer failed: book not found in source branch" çap et, false qaytar
Varsa: statusunu IN_TRANSIT et, toBranch-a köçür, sonra AVAILABLE et
Deque-yə qeyd əlavə et ("Book X: BranchA -> BranchB"); əgər Deque ölçüsü 5-i keçərsə, ən köhnəni (removeLast() və ya pollLast()) sil
Library.searchBooks(String keyword)
Bütün filialları və kitabları Iterator ilə gəz (nə stream, nə generic — sadə for/Iterator)
title, author və ya genre-də keyword varsa (case-insensitive, toLowerCase().contains()) siyahıya əlavə et
Nəticə List<Book> qaytarsın
Library.generateBranchReport(String branchId)
Çap et: cəmi kitab sayı, unikal janr sayı, aktiv loan sayı, gecikmiş loan sayı
Library.getTopActiveMembers(int topN)
Map<Member, Integer>-i gəz, ən yüksək loan sayına malik topN üzvü tap (manual sıralama — List düzəlt, Collections.sort() + custom Comparator istifadə et, generic-lərə toxunmadan sadəcə Comparator<Member> yaz)
Library.processNotifications(int currentDay)
Hər üzvün Queue<Notification>-nu gəz, mesajları console-a çap et, sonra queue-nu boşalt (poll() loop ilə)





Deliverable strukturu

MembershipType.java
CopyStatus.java
NotificationType.java
Finable.java
Notifiable.java
LibraryUser.java
Book.java
BookCopy.java
Member.java
Branch.java
Loan.java
Reservation.java
Notification.java
FineRecord.java
Library.java
Main.java
