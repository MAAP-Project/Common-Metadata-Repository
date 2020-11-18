;; WARNING: This file was generated from umm-soft-json-schema.json. Do not manually modify.
(ns cmr.umm-spec.models.umm-software-models
   "Defines UMM-Soft clojure records."
 (:require [cmr.common.dev.record-pretty-printer :as record-pretty-printer]))

(defrecord UMM-Soft
  [
   ;; Contain detailed data about the software such as input, output variables, datasets and etc.
   SoftwareDescription

   ;; General information about the updated time, published time of metadata record and language
   ;; used in the metadata of this sofware
   Metadata

   ;; Software execution
   SoftwareUsage

   ;; Software related URLs
   ScienceKeywords

   ;; General software description such as title, name, doi, authority, etc.
   Software

   ;; Software related URLs
   RelatedURLs

   ;; Software execution
   Version

   ;; Software execution
   Execute

   ;; Software execution
   Contact
  ])
(record-pretty-printer/enable-record-pretty-printing UMM-Soft)

;; Contact Information
(defrecord SoftwareContactType
  [
   ;; Contact Person Information
   ContactPerson

   ;; Contact Person Information
   ContactGroup
  ])
(record-pretty-printer/enable-record-pretty-printing SoftwareContactType)

;; For paleoclimate or geologic data, PaleoTemporalCoverage is the length of time represented by the
;; data collected. PaleoTemporalCoverage should be used when the data spans time frames earlier than
;; yyyy-mm-dd = 0001-01-01.
(defrecord PaleoTemporalCoverageType
  [
   ;; Hierarchy of terms indicating units of geologic time, i.e., eon (e.g, Phanerozoic), era (e.g.,
   ;; Cenozoic), period (e.g., Paleogene), epoch (e.g., Oligocene), and stage or age (e.g,
   ;; Chattian).
   ChronostratigraphicUnits

   ;; A string indicating the number of years furthest back in time, including units, e.g., 100 Ga.
   ;; Units may be Ga (billions of years before present), Ma (millions of years before present), ka
   ;; (thousands of years before present) or ybp (years before present).
   StartDate

   ;; A string indicating the number of years closest to the present time, including units, e.g., 10
   ;; ka. Units may be Ga (billions of years before present), Ma (millions of years before present),
   ;; ka (thousands of years before present) or ybp (years before present).
   EndDate
  ])
(record-pretty-printer/enable-record-pretty-printing PaleoTemporalCoverageType)

;; Input Software description
(defrecord InputVariableType
  [
   ;; The name(s) of the variables that are required inputs into the software. A variable is a named
   ;; set of data that contains the recorded values of a measurement. A variable can also be the
   ;; output of a model
   Name

   ;; Brief description of the input variable including any relevant scientific or technical content
   Description

   ;; Access URL
   AccessURL
  ])
(record-pretty-printer/enable-record-pretty-printing InputVariableType)

;; Software Science Keywords
(defrecord SoftwareScienceKeywordType
  [
   ;; The category of software
   Category

   ;; Software topics
   Topic

   ;; Some terms about the software
   Term

   ;; Variable Level1
   VariableLevel1

   ;; Variable Level2
   VariableLevel2

   ;; Variable Level3
   VariableLevel3

   ;; Detailed variable
   DetailedVariable
  ])
(record-pretty-printer/enable-record-pretty-printing SoftwareScienceKeywordType)

;; Software Version
(defrecord SoftwareVersionType
  [
   ;; The version number or version indicator of the software
   Version

   ;; The release date for this version of the software
   ReleaseDate

   ;; Provides a brief and meaningful description of the software version including how this
   ;; differes from the previous version
   Description

   ;; The version number or version indicator of the software this version supersedes
   Supersedes

   ;; The version number or version indicator of the software version this version is superseded by
   SupersededBy
  ])
(record-pretty-printer/enable-record-pretty-printing SoftwareVersionType)

;; Contact information
(defrecord ContactInformationType
  [
   ;; Related URLs relevant to contacting the contact person/group
   RelatedURL

   ;; Mechanism of contact information
   ContactMechanism
  ])
(record-pretty-printer/enable-record-pretty-printing ContactInformationType)

;; Contact Group Information
(defrecord ContactGroupType
  [
   ;; The contact group's role as related to the software
   Roles

   ;; The contact group's name
   GroupName

   ;; Contact Person Information
   ContactInformation

   ;; Unique identifier of the person
   UUID
  ])
(record-pretty-printer/enable-record-pretty-printing ContactGroupType)

(defrecord LocalCoordinateSystemType
  [
   ;; The information provided to register the local system to the Earth (e.g. control points,
   ;; satellite ephemeral data, and inertial navigation data).
   GeoReferenceInformation

   ;; A description of the Local Coordinate System and geo-reference information.
   Description
  ])
(record-pretty-printer/enable-record-pretty-printing LocalCoordinateSystemType)

;; Software execution access
(defrecord ExecuteAccessLicenseURLType
  [
   ;; URL of license that software is released under
   URL

   Protocol

   ;; Name of the license the software is released under
   Name

   ;; Description of the liscense the software is released under
   Description
  ])
(record-pretty-printer/enable-record-pretty-printing ExecuteAccessLicenseURLType)

;; Workflow description
(defrecord WorkflowType
  [
   ;; The name of the workflow that utilizes the software being described
   Name

   ;; Brief description of the software workflow. Should provide the overall goal of the workflow as
   ;; well as brief description of the purpose of each software component.
   Description

   ;; Describes the which step in a defined workflow this software presents
   Step
  ])
(record-pretty-printer/enable-record-pretty-printing WorkflowType)

;; Mechanism of contact information
(defrecord ContactInformationMechanismType
  [
   ;; The contact mechanism type
   Type

   ;; The actual contact mechanism value (email address, etc...)
   Value
  ])
(record-pretty-printer/enable-record-pretty-printing ContactInformationMechanismType)

;; Output Software description
(defrecord OutputVariableType
  [
   ;; The name(s) of the variables that are required output from the software. A variable is a named
   ;; set of data that contains the recorded values of a measurement. A variable can also be the
   ;; input of a model
   Name

   ;; Brief description of the output variable including any relevant scientific or technical
   ;; content
   Description

   ;; Access URL
   AccessURL
  ])
(record-pretty-printer/enable-record-pretty-printing OutputVariableType)

;; Contact information
(defrecord ContactGroupInformationType
  [
   ;; Related URLs relevant to contacting the contact person/group
   RelatedURL

   ;; Mechanism of contact information
   Mechanism
  ])
(record-pretty-printer/enable-record-pretty-printing ContactGroupInformationType)

(defrecord ChronostratigraphicUnitType
  [
   Eon

   Era

   Epoch

   Stage

   DetailedClassification

   Period
  ])
(record-pretty-printer/enable-record-pretty-printing ChronostratigraphicUnitType)

;; Software Usage
(defrecord SoftwareUsageType
  [
   ;; Describes any assumptions make for using the software
   Assumptions

   ;; Describes any limitations in using the software. May include technical as well as scientific
   ;; limitations
   Limitations

   ;; The type of software (i.e. program, source code, etc)
   Type

   ;; Free text content discribing any known bugs or issues of the software
   Provenance
  ])
(record-pretty-printer/enable-record-pretty-printing SoftwareUsageType)

;; Software related URLs
(defrecord SoftwareRelatedURLType
  [
   ;; URL that provides access to resources related to the software
   URL

   ;; Brief description of where the link navigates to and the type of information contained.
   ;; Descriptions should be unique to the link
   Description

   ;; Describes the content of a link
   URLContentType

   ;; Keyword that specifies the content of the link
   Type

   ;; Keyword that further specifies the content of the link
   Subtype

   ;; The content size (in bytes) of the representation that is expected to be returned from the URL
   size

   ;; The language of the resource pointed to by the URL
   language
  ])
(record-pretty-printer/enable-record-pretty-printing SoftwareRelatedURLType)

;; Software execution access
(defrecord ExecuteAccessType
  [
   ;; Location at which the software can be downloaded from
   URL

   ;; Description of the website from which the software can be accessed
   Description

   ;; Execute Access License URL
   LicenseURL

   ;; Any resrictions imposed on acccess to the software
   Constraints
  ])
(record-pretty-printer/enable-record-pretty-printing ExecuteAccessType)

;; Software running
(defrecord ExecuteRunType
  [
   ;; Description of the test that is being run including such information as the use case being
   ;; demonstrated and the time it takes to complete
   TestDescriptions

   ;; Instructions on how to run and validate any provided benchmark test cases
   TestInstructions

   ;; Local locatation of the data provided for benchmarking tesing of the software
   TestData

   ;; Describes the data that is being used for the benchmarking test
   TestDataDescription

   ;; Memory required to run the benchmark test provided
   TestMemory

   ;; Description of the time and processing requirements for a sample run of the benchmark test
   ;; case. Should provide a user a general understanding of the compute time and any additional
   ;; information needed to successfully run the test case
   TestRunTimeDescription

   ;; The disk space required to store output from the provided benchmark test provided
   TestOutputDiskSpace
  ])
(record-pretty-printer/enable-record-pretty-printing ExecuteRunType)

;; This element contains the Processing Level Id and the Processing Level Description
(defrecord ProcessingLevelType
  [
   ;; Description of the meaning of the Processing Level Id, e.g., the Description for the Level4
   ;; Processing Level Id might be 'Model output or results from analyses of lower level data'
   ProcessingLevelDescription

   ;; An identifier indicating the level at which the data in the collection are processed, ranging
   ;; from Level0 (raw instrument data at full resolution) to Level4 (model output or analysis
   ;; results). The value of Processing Level Id is chosen from a controlled vocabulary.
   Id
  ])
(record-pretty-printer/enable-record-pretty-printing ProcessingLevelType)

(defrecord GeographicCoordinateSystemType
  [
   ;; Units of measure used for the geodetic latitude and longitude resolution values (e.g., decimal
   ;; degrees).
   GeographicCoordinateUnits

   ;; The minimum difference between two adjacent latitude values in the Geographic Coordinate
   ;; System, expressed in Geographic Coordinate Units of measure, expressed as a two-digit decimal
   ;; number, e.g., 0.01
   LatitudeResolution

   ;; The minimum difference between two adjacent longitude values in the Geographic Coordinate
   ;; System, expressed in Geographic Coordinate Units of measure, expressed as a two-digit decimal
   ;; number, e.g., 0.01
   LongitudeResolution
  ])
(record-pretty-printer/enable-record-pretty-printing GeographicCoordinateSystemType)

;; Contact Person Information
(defrecord ContactPersonType
  [
   ;; The contact person's role as related to the software
   Roles

   ;; The first name of the contact person
   FirstName

   ;; The middle name of the contact person
   MiddleName

   ;; The last name of the contact person
   LastName

   ;; Contact Person Information
   ContactInformation

   ;; Unique identifier of the person
   UUID
  ])
(record-pretty-printer/enable-record-pretty-printing ContactPersonType)

;; The reference frame or system from which altitude or depths are measured. The term 'altitude' is
;; used instead of the common term 'elevation' to conform to the terminology in Federal Information
;; Processing Standards 70-1 and 173. The information contains the datum name, distance units and
;; encoding method, which provide the definition for the system.
(defrecord VerticalSystemDefinitionType
  [
   ;; The identification given to the level surface taken as the surface of reference from which
   ;; measurements are compared.
   DatumName

   ;; The units in which measurements are recorded.
   DistanceUnits

   ;; The means used to encode measurements.
   EncodingMethod

   ;; The minimum distance possible between two adjacent values, expressed in distance units of
   ;; measure for the collection.
   Resolutions
  ])
(record-pretty-printer/enable-record-pretty-printing VerticalSystemDefinitionType)

;; Software description
(defrecord SoftwareDescriptionType
  [
   ;; Describes the platform on which the associated instrument is contained
   platform

   ;; Describes the intrument whos data the software was developed to process
   instrument

   ;; The dataset or datasets that are valid inputs into the software
   InputDataset

   ;; Input variable
   InputVariable

   ;; The dataset or datasets that are outputs of the software
   OutputDataset

   ;; Output variable
   OutputVariable

   ;; Interoperable Software
   InteroperableSoftware
  ])
(record-pretty-printer/enable-record-pretty-printing SoftwareDescriptionType)

(defrecord GeodeticModelType
  [
   ;; The identification given to the reference system used for defining the coordinates of points.
   HorizontalDatumName

   ;; Identification given to established representation of the Earth's shape.
   EllipsoidName

   ;; Radius of the equatorial axis of the ellipsoid.
   SemiMajorAxis

   ;; The ratio of the Earth's major axis to the difference between the major and the minor.
   DenominatorOfFlatteningRatio
  ])
(record-pretty-printer/enable-record-pretty-printing GeodeticModelType)

;; General information about the updated time, published time of metadata record and language used
;; in the metadata of this sofware
(defrecord MetadataType
  [
   ;; Date of creation or last updated of the metadata record
   Updated

   ;; Metadata language
   Language

   ;; Date of the first availability of the metadata record
   Published
  ])
(record-pretty-printer/enable-record-pretty-printing MetadataType)

;; This entity stores the reference frame or system from which altitudes (elevations) are measured.
;; The information contains the datum name, distance units and encoding method, which provide the
;; definition for the system. This table also stores the characteristics of the reference frame or
;; system from which depths are measured. The additional information in the table are geometry
;; reference data etc.
(defrecord SpatialInformationType
  [
   VerticalCoordinateSystem

   HorizontalCoordinateSystem

   ;; Denotes whether the spatial coverage of the collection is horizontal, vertical, horizontal and
   ;; vertical, orbit, or vertical and orbit.
   SpatialCoverageType
  ])
(record-pretty-printer/enable-record-pretty-printing SpatialInformationType)

;; AccessURL Type
(defrecord AccessURLType
  [
   ;; Access URL location for the data
   URL

   ;; Brief description of where the link navigates to and the type of infomation contained.
   ;; Descriptions should be unique to the link
   Description
  ])
(record-pretty-printer/enable-record-pretty-printing AccessURLType)

;; Software execution
(defrecord ExecuteType
  [
   ;; Software accessing
   Access

   ;; Software installation
   Install

   ;; Software running
   Run
  ])
(record-pretty-printer/enable-record-pretty-printing ExecuteType)

(defrecord HorizontalCoordinateSystemType
  [
   GeodeticModel

   GeographicCoordinateSystem

   LocalCoordinateSystem
  ])
(record-pretty-printer/enable-record-pretty-printing HorizontalCoordinateSystemType)

;; Formerly called Internal Directory Name (IDN) Node (IDN_Node). This element has been used
;; historically by the GCMD internally to identify association, responsibility and/or ownership of
;; the dataset, service or supplemental information. Note: This field only occurs in the DIF. When a
;; DIF record is retrieved in the ECHO10 or ISO 19115 formats, this element will not be translated.
(defrecord DirectoryNameType
  [
   ShortName

   LongName
  ])
(record-pretty-printer/enable-record-pretty-printing DirectoryNameType)

;; Interoperable Software description
(defrecord InteroperableSoftwareType
  [
   ;; The name of the the workflow that utilizes the software being described
   Name

   ;; The URL
   URL

   ;; Brief description of the software workflow. Should provide the overall goal of the workflow as
   ;; well as brief description of the purpose of each software component
   Description

   ;; Workflow
   Workflow
  ])
(record-pretty-printer/enable-record-pretty-printing InteroperableSoftwareType)

;; General software description such as title, name, doi, authority, etc.
(defrecord SoftwareType
  [
   ;; The Short Name element contains an abbreviated name of the software
   ShortName

   ;; Stores the DOI that identifies the software. A DOI is a persistant identifier that is used to
   ;; uniquel identify objects
   DOI

   ;; Any citation details not included in other fields
   OtherDetails

   ;; Formatted citation for the software
   Citation

   ;; Name of the entity responsible for publishing the software
   Publisher

   ;; Descriptive formal title, or name, of the software
   EntryTitle

   ;; Formal name(s) of agencies whos funding contributed to the development of the software
   FundingSource

   ;; Brief description of the purpose of the software. This description is meant to concisely
   ;; provide users the information needed to quickly understand the relevance and userfulness of
   ;; the software
   Description

   ;; Number of times this version of the software has been downloaded
   Downloads

   ;; 
   Project
  ])
(record-pretty-printer/enable-record-pretty-printing SoftwareType)

;; Software execution access
(defrecord ExecuteInstallType
  [
   ;; Detailed documentation on how to download and install the software
   Documentation

   ;; The language(s) included in the software
   Language

   ;; The memory required to download and install the software
   Memory

   ;; List of operating systems that the software can be run on
   OperatingSystem

   ;; The average or benchmarked time (in minutes) to install the software on an operating system
   InstallationTime

   ;; Any additional dependencies, such as third party software or language specific package, that
   ;; are required to install the software
   Dependencies

   ;; Any additional information that a user may need to install the software
   AdditionalInformation
  ])
(record-pretty-printer/enable-record-pretty-printing ExecuteInstallType)

(defrecord VerticalCoordinateSystemType
  [
   AltitudeSystemDefinition

   DepthSystemDefinition
  ])
(record-pretty-printer/enable-record-pretty-printing VerticalCoordinateSystemType)
