create sequence auto_id_bases;
create table bases (id integer primary key default nextval('auto_id_bases'), baseStr text unique not null);
create sequence auto_id_suffixes;
create table suffixes (id smallint primary key default nextval('auto_id_suffixes'), suffix text unique not null);
create table partsOfSpeeches (id smallint primary key, type text unique not null);
insert into partsOfSpeeches values (1, 'существительное'), (2, 'прилагательное'), (3, 'местоимение-существительное'), (4, 'глагол'), (5, 'причастие'), (6, 'деепричастие'), (7, 'инфинитив'), (8, 'местоимение-предикатив'), (9, 'местоименное-прилагательное'), 
(10, 'числительное (количественное)'), (11, 'порядковое числительное'), (12, 'наречие'), (13, 'предикат'), (14, 'предлог'), 
(15, 'союз'), (16, 'междометие'), (17, 'частица'), (18, 'вводное слово'), (19, 'краткое прилагательное'), (20, 'краткое причастие');
create table genders (id smallint primary key, type text unique not null);
insert into genders values (1, 'мужской род'), (2, 'женский род'), (3, 'женский род');
create table animacy (id boolean primary key, type text unique not null);
insert into animacy values ('true', 'одушевленное'), ('false', 'неодушевленное');
create table count (id boolean primary key, type text unique not null);
insert into count values ('true', 'единственное число'), ('false', 'множественное число');
create table cases (id smallint primary key, type text unique not null);
insert into cases values (1, 'именительный падеж'), (2, 'родительный падеж'), (3, 'дательный падеж'), (4, 'винительный падеж'), 
(5, 'творительный падеж'), (6, 'предложный падеж'), (7, 'звательный падеж'), 
(8, 'второй родительный падеж или второй предложный падеж');
create table aspects (id boolean primary key, type text unique not null);
insert into aspects values ('true', 'совершенный вид'), ('false', 'несовершенный вид');
create table typesOfVerbs (id boolean primary key, type text unique not null);
insert into typesOfVerbs values ('true', 'переходный'), ('false', 'непереходный');
create table typesOfVoices (id boolean primary key, type text unique not null);
insert into typesOfVoices values ('true', 'действительный залог'), ('false', 'страдательный залог');
create table tenses (id smallint primary key, type text unique not null);
insert into tenses values (1, 'настоящее время'), (2, 'прошедшее время'), (3, 'будущее время');
create table imperativeMood (id boolean primary key, type text unique not null);
insert into imperativeMood values ('true', 'повелительное наклонение');
create table typesOfPronouns (id smallint primary key, type text unique not null);
insert into typesOfPronouns values (1, 'первое лицо'), (2, 'второе лицо'), (3, 'третье лицо');
create table unchanging (id boolean primary key, type text unique not null);
insert into unchanging values ('true', 'неизменяемое');
create table shortAd (id boolean primary key, type text unique not null);
insert into shortAd values ('true', 'краткое');
create table comparativeAdjective (id boolean primary key, type text unique not null);
insert into comparativeAdjective values ('true', 'сравнительная форма');
create table typesOfNames (id smallint primary key, type text unique not null);
insert into typesOfNames values (1, 'имя'), (2, 'фамилия'), (3, 'отчество');
create table locativeOrOrganization (id boolean primary key, type text unique not null);
insert into locativeOrOrganization values ('true', 'локативность'), ('false', 'организация');
create table qualitativeAdjective (id boolean primary key, type text unique not null);
insert into qualitativeAdjective values ('true', 'качественное');
create table interrogativeRelativeAdverb (id boolean primary key, type text unique not null);
insert into interrogativeRelativeAdverb values ('true', 'вопросительное'), ('false', 'относительное');
create table noPlural (id boolean primary key, type text unique not null);
insert into noPlural values ('true', 'не имеет множественного числа');
create table typo (id boolean primary key, type text unique not null);
insert into typo values ('true', 'присуща частая опечатка или ошибка');
create table jargonArchaicProfessionalism (id smallint primary key, type text unique not null);
insert into jargonArchaicProfessionalism values (1, 'жаргонизм'), (2, 'архаизм'), (3, 'профессионализм');
create table abbreviation (id boolean primary key, type text unique not null);
insert into abbreviation values ('true', 'аббревиатура');
create table impersonalVerb (id boolean primary key, type text unique not null);
insert into impersonalVerb values ('true', 'безличный глагол');
create sequence auto_id_grammeminfo;
create table grammemInfo (id integer primary key default nextval('auto_id_grammeminfo'), baseStrId integer references bases(id), 
suffixId smallint references suffixes(id), 
partOfSpeechId smallint references partsOfSpeeches (id),
genderId smallint references genders (id),
animacyId boolean references animacy (id),
countId boolean references count (id),
caseId smallint references cases (id),
aspectId boolean references aspects (id), 
typeOfVerbId boolean references typesOfVerbs (id),
typeOfVoiceId boolean references typesOfVoices (id),
tenseId smallint references tenses (id),
imperativeMood boolean references imperativeMood (id),
typeOfPronounId smallint references typesOfPronouns (id),
unchanging boolean references unchanging (id),
shortAdId boolean references shortAd (id),
comparativeAdjective boolean references comparativeAdjective (id),
typeOfNameId smallint references typesOfNames (id),
locativeOrOrganizationId boolean references locativeOrOrganization (id),
qualitativeAdjective boolean references qualitativeAdjective (id),
interrogativeRelativeAdverbId boolean references interrogativeRelativeAdverb (id),
noPlural boolean references noPlural (id),
typo boolean references typo (id),
jargonArchaicProfessionalismId smallint references jargonArchaicProfessionalism (id),
abbreviation boolean references abbreviation (id),
impersonalVerb boolean references impersonalVerb (id)
);
