

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;


CREATE TABLE public.course_prerequisites (
    course_id integer NOT NULL,
    prerequisite_course_id integer NOT NULL
);


ALTER TABLE public.course_prerequisites OWNER TO postgres;



CREATE TABLE public.courses (
    course_id integer NOT NULL,
    course_prefix character varying(10),
    course_number character varying(10),
    course_name character varying(255),
    hours integer,
    times_to_take integer
);


ALTER TABLE public.courses OWNER TO postgres;


CREATE VIEW public."Courses with Prerequisites" AS
 SELECT c1.course_name,
    c1.course_id,
    cp.prerequisite_course_id,
    c2.course_name AS prerequisite_name
   FROM ((public.course_prerequisites cp
     JOIN public.courses c1 ON ((cp.course_id = c1.course_id)))
     JOIN public.courses c2 ON ((cp.prerequisite_course_id = c2.course_id)))
  ORDER BY c1.course_id;


ALTER TABLE public."Courses with Prerequisites" OWNER TO postgres;



CREATE TABLE public.course_enrollments (
    enrollment_id integer NOT NULL,
    course_id integer,
    user_id integer
);


ALTER TABLE public.course_enrollments OWNER TO postgres;



CREATE SEQUENCE public.course_enrollments_enrollment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.course_enrollments_enrollment_id_seq OWNER TO postgres;



ALTER SEQUENCE public.course_enrollments_enrollment_id_seq OWNED BY public.course_enrollments.enrollment_id;



CREATE TABLE public.major_courses (
    major_id integer,
    course_id integer
);


ALTER TABLE public.major_courses OWNER TO postgres;


CREATE TABLE public.majors (
    major_id integer NOT NULL,
    major character varying(50)
);


ALTER TABLE public.majors OWNER TO postgres;


CREATE SEQUENCE public.majors_major_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.majors_major_id_seq OWNER TO postgres;


ALTER SEQUENCE public.majors_major_id_seq OWNED BY public.majors.major_id;


CREATE TABLE public.student_advisor (
    student_id integer NOT NULL,
    advisor_id integer NOT NULL
);


ALTER TABLE public.student_advisor OWNER TO postgres;


CREATE TABLE public.student_major (
    student_id integer NOT NULL,
    major_id integer NOT NULL
);


ALTER TABLE public.student_major OWNER TO postgres;


CREATE TABLE public.students (
    student_id integer NOT NULL,
    first_name character varying(50),
    last_name character varying(50)
);


ALTER TABLE public.students OWNER TO postgres;

CREATE SEQUENCE public.students_student_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.students_student_id_seq OWNER TO postgres;

ALTER SEQUENCE public.students_student_id_seq OWNED BY public.students.student_id;

CREATE TABLE public.users (
    user_id integer NOT NULL,
    username character varying(50) NOT NULL,
    password_hash character varying(200) NOT NULL,
    role character varying(50) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;


CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_user_id_seq OWNER TO postgres;


ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


ALTER TABLE ONLY public.course_enrollments ALTER COLUMN enrollment_id SET DEFAULT nextval('public.course_enrollments_enrollment_id_seq'::regclass);

ALTER TABLE ONLY public.majors ALTER COLUMN major_id SET DEFAULT nextval('public.majors_major_id_seq'::regclass);


ALTER TABLE ONLY public.students ALTER COLUMN student_id SET DEFAULT nextval('public.students_student_id_seq'::regclass);


ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);

COPY public.course_prerequisites (course_id, prerequisite_course_id) FROM stdin;
25	17
27	19
32	22
32	21
26	18
26	17
28	20
28	19
33	23
34	23
34	24
40	23
39	28
40	24
40	33
41	23
41	24
41	33
41	34
42	23
42	24
42	33
42	34
52	23
52	24
52	33
52	34
52	40
53	23
53	24
53	33
53	34
53	40
53	42
44	18
11	10
20	19
24	23
22	21
18	17
12	11
27	20
25	18
28	27
58	57
32	31
26	25
43	18
35	28
44	43
37	26
51	35
53	41
45	36
45	58
45	4
45	5
45	6
9	36
9	58
9	4
9	5
9	6
8	36
8	58
8	4
8	5
8	6
7	36
7	58
7	4
7	5
7	6
\.


COPY public.courses (course_id, course_prefix, course_number, course_name, hours, times_to_take) FROM stdin;
16	MU	100	Recital	1	7
2	BY	101	General Biology	3	1
17	MU	101	Theory I	2	1
1	FYA	101	Orientation to the Academy	1	1
10	EN	101	English Composition I	3	1
13	HI	101	World Civilization	3	1
15	MA	101	Intermediate Algebra	3	1
3	BY	101L	General Biology Lab	1	1
18	MU	102	Theory II	2	1
11	EN	102	English Composition II	3	1
19	MU	103	Ear Train/Sight Seeing I	1	1
20	MU	104	Ear Train/Sight Seeing II	1	1
59	SPE	111	Fundamentals of Speech	3	1
21	MU	131	Keyboard Musicianship I	2	1
22	MU	132	Keyboard Musicianship II	2	1
23	MU	141	Applied Music	1	1
24	MU	142	Applied Music	1	1
14	HUM	201	Humanities	3	1
12	EN	201	Introduction to Literature	3	1
56	PS	201	Intro to American Government	3	1
57	PSY	201	General Psychology	3	1
25	MU	201	Theory III	2	1
26	MU	202	Theory IV	2	1
27	MU	204	Eye Train/Sight Singing III	1	1
28	MU	205	Ear Train/Sight Singing IV	1	1
29	MU	216	Small Ensemble	1	1
30	MU	221	Large Ensemble	1	4
31	MU	231	Keyboard Musicianship III	2	1
32	MU	232	Keyboard Musicianship IV	2	1
33	MU	241	Applied Music	1	1
34	MU	242	Applied Music	1	1
4	ED	300	Technology for Teachers	3	1
58	PSY	301	Educational Psychology	3	1
35	MU	302	Basic Conducting	2	1
36	MU	303	Intro to Music Teaching	3	1
37	MU	304	Form and Analysis	2	1
38	MU	305	Music Tech	2	1
5	ED	319	Excep Child	3	1
39	MU	321	Voice Diction	2	1
40	MU	341	Applied Music	1	1
41	MU	342	Pre-Recital Hearing	1	1
42	MU	343	Junior Recital	1	1
6	ED	350	Soc Hist & Phil Found	3	1
43	MU	351	Music History/Lit I	2	1
44	MU	352	Music History/Lit II	2	1
45	MU	356	P-12 Instr Method	3	1
46	MU	371	Woodwind Method	2	1
47	MU	372	String Method	2	1
48	MU	373	Percussion Method	2	1
49	MU	374	Brass Methods	2	1
50	MU	399	Writing about Music	2	1
51	MU	404	Adv Instr Conducting	2	1
7	ED	412	Reading Across the Curriculum	3	1
52	MU	413	Senior Recital	1	1
53	MU	441	Applied Music	1	1
54	MU	444	Senior Seminar	1	1
8	ED	445	Measure and Evaluations	3	1
55	MU	445	Directed Teaching	12	1
9	ED	448	Classroom Management	3	1
\.


COPY public.major_courses (major_id, course_id) FROM stdin;
1	1
1	2
1	3
1	4
1	5
1	6
1	7
1	8
1	9
1	10
1	11
1	12
1	13
1	14
1	15
1	16
1	17
1	18
1	19
1	20
1	21
1	22
1	23
1	24
1	25
1	26
1	27
1	28
1	29
1	30
1	31
1	32
1	33
1	34
1	35
1	36
1	37
1	38
1	39
1	40
1	41
1	42
1	43
1	44
1	45
1	46
1	47
1	48
1	49
1	50
1	51
1	52
1	53
1	54
1	55
1	56
1	57
1	58
1	59
\.


COPY public.majors (major_id, major) FROM stdin;
1	Music Education - Instrumental
\.


COPY public.users (user_id, username, password_hash, role) FROM stdin;
1	user	$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC	ROLE_USER
2	admin	$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC	ROLE_ADMIN
\.


SELECT pg_catalog.setval('public.course_enrollments_enrollment_id_seq', 1, false);


SELECT pg_catalog.setval('public.majors_major_id_seq', 1, true);


SELECT pg_catalog.setval('public.students_student_id_seq', 21, true);


SELECT pg_catalog.setval('public.users_user_id_seq', 2, true);


ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT course_enrollments_pkey PRIMARY KEY (enrollment_id);


ALTER TABLE ONLY public.course_prerequisites
    ADD CONSTRAINT course_prerequisites_pkey PRIMARY KEY (course_id, prerequisite_course_id);


ALTER TABLE ONLY public.majors
    ADD CONSTRAINT majors_pkey PRIMARY KEY (major_id);


ALTER TABLE ONLY public.courses
    ADD CONSTRAINT pk_course_id PRIMARY KEY (course_id);


ALTER TABLE ONLY public.users
    ADD CONSTRAINT pk_user PRIMARY KEY (user_id);


ALTER TABLE ONLY public.student_advisor
    ADD CONSTRAINT student_advisor_pkey PRIMARY KEY (student_id, advisor_id);


ALTER TABLE ONLY public.student_major
    ADD CONSTRAINT student_major_pkey PRIMARY KEY (student_id, major_id);


ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (student_id);


ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT course_enrollments_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


ALTER TABLE ONLY public.major_courses
    ADD CONSTRAINT course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(course_id);


ALTER TABLE ONLY public.course_prerequisites
    ADD CONSTRAINT course_prerequisites_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(course_id);


ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT fk_course_id FOREIGN KEY (course_id) REFERENCES public.courses(course_id);


ALTER TABLE ONLY public.course_prerequisites
    ADD CONSTRAINT fk_prerequisite FOREIGN KEY (prerequisite_course_id) REFERENCES public.courses(course_id);


ALTER TABLE ONLY public.student_advisor
    ADD CONSTRAINT fk_student_advisor_student_id FOREIGN KEY (student_id) REFERENCES public.students(student_id) ON DELETE CASCADE;


ALTER TABLE ONLY public.student_major
    ADD CONSTRAINT fk_student_major_student_id FOREIGN KEY (student_id) REFERENCES public.students(student_id) ON DELETE CASCADE;


ALTER TABLE ONLY public.major_courses
    ADD CONSTRAINT major_id_fkey FOREIGN KEY (major_id) REFERENCES public.majors(major_id);


ALTER TABLE ONLY public.student_advisor
    ADD CONSTRAINT student_advisor_advisor_id_fkey FOREIGN KEY (advisor_id) REFERENCES public.users(user_id);


ALTER TABLE ONLY public.student_major
    ADD CONSTRAINT student_major_major_id_fkey FOREIGN KEY (major_id) REFERENCES public.majors(major_id);


GRANT ALL ON TABLE public.course_prerequisites TO owner;


GRANT SELECT,INSERT,REFERENCES,DELETE,TRIGGER,UPDATE ON TABLE public.courses TO owner;


GRANT ALL ON TABLE public.course_enrollments TO owner;


GRANT SELECT,USAGE ON SEQUENCE public.course_enrollments_enrollment_id_seq TO owner;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.majors TO PUBLIC;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.student_advisor TO PUBLIC;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.student_major TO PUBLIC;


GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.students TO PUBLIC;


GRANT SELECT,USAGE ON SEQUENCE public.students_student_id_seq TO PUBLIC;

GRANT ALL ON TABLE public.users TO owner;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.users TO appuser;


GRANT ALL ON SEQUENCE public.users_user_id_seq TO owner;
GRANT SELECT,USAGE ON SEQUENCE public.users_user_id_seq TO appuser;


