package io.katharsis.client.mock.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.katharsis.client.mock.models.Project;
import io.katharsis.client.mock.models.Project.ProjectLinks;
import io.katharsis.client.mock.models.Project.ProjectMeta;
import io.katharsis.errorhandling.exception.ResourceNotFoundException;
import io.katharsis.legacy.queryParams.QueryParams;
import io.katharsis.legacy.repository.LinksRepository;
import io.katharsis.legacy.repository.MetaRepository;
import io.katharsis.legacy.repository.ResourceRepository;
import io.katharsis.resource.links.LinksInformation;
import io.katharsis.resource.meta.MetaInformation;

public class ProjectRepository implements ResourceRepository<Project, Long>, MetaRepository<Project>, LinksRepository<Project> {

	private static final ConcurrentHashMap<Long, Project> THREAD_LOCAL_REPOSITORY = new ConcurrentHashMap<>();

	public static void clear() {
		THREAD_LOCAL_REPOSITORY.clear();
	}

	@Override
	public <S extends Project> S save(S entity) {
		entity.setId((long) (THREAD_LOCAL_REPOSITORY.size() + 1));
		THREAD_LOCAL_REPOSITORY.put(entity.getId(), entity);

		ProjectLinks links = new ProjectLinks();
		links.setValue("someLinkValue");
		entity.setLinks(links);

		ProjectMeta meta = new ProjectMeta();
		meta.setValue("someMetaValue");
		entity.setMeta(meta);

		return entity;
	}

	@Override
	public Project findOne(Long aLong, QueryParams queryParams) {
		Project project = THREAD_LOCAL_REPOSITORY.get(aLong);
		if (project == null) {
			throw new ResourceNotFoundException(Project.class.getCanonicalName());
		}
		return project;
	}

	@Override
	public Iterable<Project> findAll(QueryParams queryParamss) {
		return THREAD_LOCAL_REPOSITORY.values();
	}

	@Override
	public Iterable<Project> findAll(Iterable<Long> ids, QueryParams queryParams) {
		List<Project> values = new LinkedList<>();
		for (Project value : THREAD_LOCAL_REPOSITORY.values()) {
			if (contains(value, ids)) {
				values.add(value);
			}
		}
		return values;
	}

	private boolean contains(Project value, Iterable<Long> ids) {
		for (Long id : ids) {
			if (value.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void delete(Long aLong) {
		THREAD_LOCAL_REPOSITORY.remove(aLong);
	}

	@Override
	public LinksInformation getLinksInformation(Iterable<Project> resources, QueryParams queryParams) {
		ProjectsLinksInformation info = new ProjectsLinksInformation();
		info.setLinkValue("testLink");
		return info;
	}

	@Override
	public MetaInformation getMetaInformation(Iterable<Project> resources, QueryParams queryParams) {
		ProjectsMetaInformation info = new ProjectsMetaInformation();
		info.setMetaValue("testMeta");
		return info;
	}

	public static class ProjectsLinksInformation implements LinksInformation {

		private String linkValue;

		public String getLinkValue() {
			return linkValue;
		}

		public void setLinkValue(String linkValue) {
			this.linkValue = linkValue;
		}
	}

	public static class ProjectsMetaInformation implements MetaInformation {

		private String metaValue;

		public String getMetaValue() {
			return metaValue;
		}

		public void setMetaValue(String metaValue) {
			this.metaValue = metaValue;
		}
	}
}
