--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 15.3

-- Started on 2023-09-12 11:54:06

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

--
-- TOC entry 218 (class 1259 OID 16523)
-- Name: course_prerequisites; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.course_prerequisites (
    course_id integer NOT NULL,
    prerequisite_course_id integer NOT NULL
);


ALTER TABLE public.course_prerequisites OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16606)
-- Name: courses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.courses (
    course_id integer NOT NULL,
    course_prefix character varying(10),
    course_number character varying(10),
    course_name character varying(255),
    hours integer,
    times_to_take integer
);


ALTER TABLE public.courses OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16609)
-- Name: Courses with Prerequisites; Type: VIEW; Schema: public; Owner: postgres
--

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

--
-- TOC entry 217 (class 1259 OID 16507)
-- Name: course_enrollments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.course_enrollments (
    enrollment_id integer NOT NULL,
    course_id integer,
    user_id integer
);


ALTER TABLE public.course_enrollments OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16506)
-- Name: course_enrollments_enrollment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.course_enrollments_enrollment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.course_enrollments_enrollment_id_seq OWNER TO postgres;

--
-- TOC entry 3401 (class 0 OID 0)
-- Dependencies: 216
-- Name: course_enrollments_enrollment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.course_enrollments_enrollment_id_seq OWNED BY public.course_enrollments.enrollment_id;


--
-- TOC entry 225 (class 1259 OID 16656)
-- Name: majors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.majors (
    major_id integer NOT NULL,
    major character varying(50)
);


ALTER TABLE public.majors OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16655)
-- Name: majors_major_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.majors_major_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.majors_major_id_seq OWNER TO postgres;

--
-- TOC entry 3403 (class 0 OID 0)
-- Dependencies: 224
-- Name: majors_major_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.majors_major_id_seq OWNED BY public.majors.major_id;


--
-- TOC entry 223 (class 1259 OID 16640)
-- Name: student_advisor; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student_advisor (
    student_id integer NOT NULL,
    advisor_id integer NOT NULL
);


ALTER TABLE public.student_advisor OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16662)
-- Name: student_major; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student_major (
    student_id integer NOT NULL,
    major_id integer NOT NULL
);


ALTER TABLE public.student_major OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16634)
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.students (
    student_id integer NOT NULL,
    first_name character varying(50),
    last_name character varying(50)
);


ALTER TABLE public.students OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16633)
-- Name: students_student_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.students_student_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.students_student_id_seq OWNER TO postgres;

--
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 221
-- Name: students_student_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.students_student_id_seq OWNED BY public.students.student_id;


--
-- TOC entry 215 (class 1259 OID 16464)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    username character varying(50) NOT NULL,
    password_hash character varying(200) NOT NULL,
    role character varying(50) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 16463)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_user_id_seq OWNER TO postgres;

--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 214
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 3209 (class 2604 OID 16510)
-- Name: course_enrollments enrollment_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_enrollments ALTER COLUMN enrollment_id SET DEFAULT nextval('public.course_enrollments_enrollment_id_seq'::regclass);


--
-- TOC entry 3211 (class 2604 OID 16659)
-- Name: majors major_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.majors ALTER COLUMN major_id SET DEFAULT nextval('public.majors_major_id_seq'::regclass);


--
-- TOC entry 3210 (class 2604 OID 16637)
-- Name: students student_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students ALTER COLUMN student_id SET DEFAULT nextval('public.students_student_id_seq'::regclass);


--
-- TOC entry 3208 (class 2604 OID 16467)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 3384 (class 0 OID 16507)
-- Dependencies: 217
-- Data for Name: course_enrollments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.course_enrollments (enrollment_id, course_id, user_id) FROM stdin;
\.


--
-- TOC entry 3385 (class 0 OID 16523)
-- Dependencies: 218
-- Data for Name: course_prerequisites; Type: TABLE DATA; Schema: public; Owner: postgres
--

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


--
-- TOC entry 3386 (class 0 OID 16606)
-- Dependencies: 219
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: postgres
--

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


--
-- TOC entry 3391 (class 0 OID 16656)
-- Dependencies: 225
-- Data for Name: majors; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.majors (major_id, major) FROM stdin;
\.


--
-- TOC entry 3389 (class 0 OID 16640)
-- Dependencies: 223
-- Data for Name: student_advisor; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student_advisor (student_id, advisor_id) FROM stdin;
\.


--
-- TOC entry 3392 (class 0 OID 16662)
-- Dependencies: 226
-- Data for Name: student_major; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student_major (student_id, major_id) FROM stdin;
\.


--
-- TOC entry 3388 (class 0 OID 16634)
-- Dependencies: 222
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.students (student_id, first_name, last_name) FROM stdin;
\.


--
-- TOC entry 3382 (class 0 OID 16464)
-- Dependencies: 215
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, username, password_hash, role) FROM stdin;
1	user	$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC	ROLE_USER
2	admin	$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC	ROLE_ADMIN
\.


--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 216
-- Name: course_enrollments_enrollment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.course_enrollments_enrollment_id_seq', 1, false);


--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 224
-- Name: majors_major_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.majors_major_id_seq', 1, false);


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 221
-- Name: students_student_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.students_student_id_seq', 1, false);


--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 214
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 2, true);


--
-- TOC entry 3217 (class 2606 OID 16512)
-- Name: course_enrollments course_enrollments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT course_enrollments_pkey PRIMARY KEY (enrollment_id);


--
-- TOC entry 3219 (class 2606 OID 16697)
-- Name: course_prerequisites course_prerequisites_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_prerequisites
    ADD CONSTRAINT course_prerequisites_pkey PRIMARY KEY (course_id, prerequisite_course_id);


--
-- TOC entry 3227 (class 2606 OID 16661)
-- Name: majors majors_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.majors
    ADD CONSTRAINT majors_pkey PRIMARY KEY (major_id);


--
-- TOC entry 3221 (class 2606 OID 16678)
-- Name: courses pk_course_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT pk_course_id PRIMARY KEY (course_id);


--
-- TOC entry 3213 (class 2606 OID 16469)
-- Name: users pk_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT pk_user PRIMARY KEY (user_id);


--
-- TOC entry 3225 (class 2606 OID 16644)
-- Name: student_advisor student_advisor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_advisor
    ADD CONSTRAINT student_advisor_pkey PRIMARY KEY (student_id, advisor_id);


--
-- TOC entry 3229 (class 2606 OID 16666)
-- Name: student_major student_major_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_major
    ADD CONSTRAINT student_major_pkey PRIMARY KEY (student_id, major_id);


--
-- TOC entry 3223 (class 2606 OID 16639)
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (student_id);


--
-- TOC entry 3215 (class 2606 OID 16471)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 3230 (class 2606 OID 16590)
-- Name: course_enrollments course_enrollments_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT course_enrollments_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 3232 (class 2606 OID 16679)
-- Name: course_prerequisites course_prerequisites_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_prerequisites
    ADD CONSTRAINT course_prerequisites_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(course_id);


--
-- TOC entry 3231 (class 2606 OID 16703)
-- Name: course_enrollments fk_course_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_enrollments
    ADD CONSTRAINT fk_course_id FOREIGN KEY (course_id) REFERENCES public.courses(course_id);


--
-- TOC entry 3233 (class 2606 OID 16698)
-- Name: course_prerequisites fk_prerequisite; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.course_prerequisites
    ADD CONSTRAINT fk_prerequisite FOREIGN KEY (prerequisite_course_id) REFERENCES public.courses(course_id);


--
-- TOC entry 3234 (class 2606 OID 16650)
-- Name: student_advisor student_advisor_advisor_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_advisor
    ADD CONSTRAINT student_advisor_advisor_id_fkey FOREIGN KEY (advisor_id) REFERENCES public.users(user_id);


--
-- TOC entry 3235 (class 2606 OID 16645)
-- Name: student_advisor student_advisor_student_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_advisor
    ADD CONSTRAINT student_advisor_student_id_fkey FOREIGN KEY (student_id) REFERENCES public.students(student_id);


--
-- TOC entry 3236 (class 2606 OID 16672)
-- Name: student_major student_major_major_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_major
    ADD CONSTRAINT student_major_major_id_fkey FOREIGN KEY (major_id) REFERENCES public.majors(major_id);


--
-- TOC entry 3237 (class 2606 OID 16667)
-- Name: student_major student_major_student_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_major
    ADD CONSTRAINT student_major_student_id_fkey FOREIGN KEY (student_id) REFERENCES public.students(student_id);


--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE course_prerequisites; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.course_prerequisites TO owner;


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 219
-- Name: TABLE courses; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,DELETE,TRIGGER,UPDATE ON TABLE public.courses TO owner;


--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE course_enrollments; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.course_enrollments TO owner;


--
-- TOC entry 3402 (class 0 OID 0)
-- Dependencies: 216
-- Name: SEQUENCE course_enrollments_enrollment_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE public.course_enrollments_enrollment_id_seq TO owner;


--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE users; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE public.users TO owner;
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.users TO appuser;


--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 214
-- Name: SEQUENCE users_user_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE public.users_user_id_seq TO owner;
GRANT SELECT,USAGE ON SEQUENCE public.users_user_id_seq TO appuser;


-- Completed on 2023-09-12 11:54:06

--
-- PostgreSQL database dump complete
--

