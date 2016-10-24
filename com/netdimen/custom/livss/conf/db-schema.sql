CREATE TABLE livss_practice_area(
	areaid			nvarchar(85)	not null,
	area_name		nvarchar(500)	not null,
	area_desc		ntext			null,
	constraint PK_PRACTICE_AREA primary key (areaid)
)

CREATE TABLE livss_firm_area(
	orgid			nvarchar(85)	not null,
	areaid			nvarchar(85)	not null,
	constraint PK_FIRM_AREA primary key (orgid, areaid),
    constraint FK_FIRM_AREA_AREA foreign key (areaid) references livss_practice_area (areaid)
)

insert into livss_practice_area values(1,'Alternative Investment Funds','') 
insert into livss_practice_area values(2,'Anti Trust / Competition','') 
insert into livss_practice_area values(3,'Aviation','') 
insert into livss_practice_area values(4,'Banking & Finance','') 
insert into livss_practice_area values(5,'Capital Markets','') 
insert into livss_practice_area values(6,'Compliance','') 
insert into livss_practice_area values(7,'Corporate & M&A','')  
insert into livss_practice_area values(8,'Employment','')  
insert into livss_practice_area values(9,'Energy & Natural Resources','') 
insert into livss_practice_area values(10,'Intellectual Property','') 
insert into livss_practice_area values(11,'Life Science','') 
insert into livss_practice_area values(12,'Litigation and Dispute resolution','')  
insert into livss_practice_area values(13,'Maritime & Shipping','')  
insert into livss_practice_area values(14,'Projects and Project Finance','')  
insert into livss_practice_area values(15,'Real Estate','')  
insert into livss_practice_area values(16,'Regulatory/Government','') 
insert into livss_practice_area values(17,'Restructuring & Insolvency','') 
insert into livss_practice_area values(18,'Taxation','') 
insert into livss_practice_area values(19,'TMT (Technology, Media, Telecommunications)','') 