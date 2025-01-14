--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3 (Debian 16.3-1.pgdg120+1)
-- Dumped by pg_dump version 17.0 (Ubuntu 17.0-1.pgdg22.04+1)

--

INSERT INTO public.charge_plan (id, name, team_size, project_size, storage, support_type, money) VALUES (1, 'basic plan', 10, 10, 20, 'basic', 50);
INSERT INTO public.charge_plan (id, name, team_size, project_size, storage, support_type, money) VALUES (2, 'pro plan', 50, 50, 100, 'pro', 100);
INSERT INTO public.charge_plan (id, name, team_size, project_size, storage, support_type, money) VALUES (3, ' prenium plan', 200, 200, 500, '24/7', 300);


--
-- Name: charge_plan_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.charge_plan_id_seq', 3, true);


--
-- PostgreSQL database dump complete
--

