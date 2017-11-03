/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ambari.server.orm.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


@IdClass(ServiceGroupEntityPK.class)
@Table(name = "servicegroups")
@NamedQueries({
  @NamedQuery(name = "serviceGroupByClusterAndServiceGroupIds", query =
    "SELECT serviceGroup " +
      "FROM ServiceGroupEntity serviceGroup " +
      "JOIN serviceGroup.clusterEntity cluster " +
      "WHERE serviceGroup.serviceGroupId=:serviceGroupId AND cluster.clusterId=:clusterId")
})
@Entity
@TableGenerator(name = "service_group_id_generator",
  table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value"
  , pkColumnValue = "service_group_id_seq"
  , initialValue = 1
)
public class ServiceGroupEntity {

  @Id
  @Column(name = "cluster_id", nullable = false, insertable = false, updatable = false, length = 10)
  private Long clusterId;

  @Id
  @Column(name = "id", nullable = false, insertable = true, updatable = true)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "service_group_id_generator")
  private Long serviceGroupId;

  @Column(name = "service_group_name", nullable = false, insertable = true, updatable = true)
  private String serviceGroupName;

  @ManyToOne
  @JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false)
  private ClusterEntity clusterEntity;

  @ManyToMany
  @JoinTable(
    name = "servicegroupdependencies",
    joinColumns = {@JoinColumn(name = "service_group_id", referencedColumnName = "id", nullable = false),
                   @JoinColumn(name = "service_group_cluster_id", referencedColumnName = "cluster_id", nullable = false)},
    inverseJoinColumns = {@JoinColumn(name = "dependent_service_group_id", referencedColumnName = "id", nullable = false),
                          @JoinColumn(name = "dependent_service_group_cluster_id", referencedColumnName = "cluster_id", nullable = false)}
  )
  private List<ServiceGroupEntity> serviceGroupDependencies = new ArrayList<>();

  @ManyToMany(mappedBy="serviceGroupDependencies")
  private List<ServiceGroupEntity> dependencies = new ArrayList<>();

  public Long getClusterId() {
    return clusterId;
  }

  public void setClusterId(Long clusterId) {
    this.clusterId = clusterId;
  }

  public Long getServiceGroupId() {
    return serviceGroupId;
  }

  public void setServiceGroupId(Long serviceGroupId) {
    this.serviceGroupId = serviceGroupId;
  }


  public String getServiceGroupName() {
    return serviceGroupName;
  }

  public void setServiceGroupName(String serviceGroupName) {
    this.serviceGroupName = serviceGroupName;
  }

  public List<ServiceGroupEntity> getServiceGroupDependencies() {
    return serviceGroupDependencies;
  }

  public void setServiceGroupDependencies(List<ServiceGroupEntity> serviceGroupDependencies) {
    this.serviceGroupDependencies = serviceGroupDependencies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ServiceGroupEntity that = (ServiceGroupEntity) o;

    if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null) return false;
    if (serviceGroupName != null ? !serviceGroupName.equals(that.serviceGroupName) : that.serviceGroupName != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = clusterId != null ? clusterId.intValue() : 0;
    result = 31 * result + (serviceGroupName != null ? serviceGroupName.hashCode() : 0);
    return result;
  }

  public ClusterEntity getClusterEntity() {
    return clusterEntity;
  }

  public void setClusterEntity(ClusterEntity clusterEntity) {
    this.clusterEntity = clusterEntity;
  }

}